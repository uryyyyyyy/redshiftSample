package com.github.uryyyyyyy.redshift

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}
import java.nio.file.{Files, Paths}
import java.util.zip.GZIPOutputStream

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}
import com.typesafe.config.ConfigFactory
import scalikejdbc._
object Main {

	val config = ConfigFactory.load()

	def main(args: Array[String]) {
		copySample()
	}

	def redshiftSample(): Unit ={
		// initialize JDBC driver & connection pool
		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		val name = "Alice"

		val effect = DB localTx { implicit session =>

			sql"DROP TABLE IF EXISTS members".execute.apply()

			sql"""
create table members (
  id BIGINT IDENTITY PRIMARY KEY,
  name varchar(64) not null,
  created_at timestamp not null
)
""".execute.apply()

			sql"insert into members (name, created_at) values (${name}, 'now')"
				.update.apply()

			val memberId = sql"select id from members where name = ${name}" // don't worry, prevents SQL injection
				.map(rs => rs.long("id")) // extracts values from rich java.sql.ResultSet
				.single // single, list, traversable
				.apply() // Side effect!!! runs the SQL using Connection

			println(memberId)
		}
	}

	def putToS3(file:File, key: String): Unit ={
		val credentials = new BasicAWSCredentials(config.getString("awsAccessKey"), config.getString("awsSecretKey"))
		val s3client = new AmazonS3Client(credentials)
//		val gzippedFile = gzipFile(file)

//		val metadata = new ObjectMetadata()
//		metadata.setContentType("text/plane")
//		metadata.setContentEncoding("gzip")
//		metadata.setContentLength(gzippedFile.length)

		s3client.putObject(new PutObjectRequest(config.getString("s3Bucket"), key, file))
	}

	def gzipFile(file:File):Array[Byte] = {
		val bytes = Files.readAllBytes(Paths.get(file.getPath))
		val baos = new ByteArrayOutputStream()
		val out = new GZIPOutputStream(baos)

		out.write(bytes, 0, bytes.length)
		out.finish()
		baos.toByteArray
	}

	def copy(key:String, table:String)(implicit session:DBSession): Unit ={

		val sql = s"""
			 |COPY ${table} FROM 's3://${config.getString("s3Bucket")}/${key}'
			 |CREDENTIALS 'aws_access_key_id=${config.getString("awsAccessKey")};aws_secret_access_key=${config.getString("awsSecretKey")}'
			 |json 'auto';
      """.stripMargin
		println(sql)
		SQL(sql).execute.apply()
	}

	def copySample(): Unit ={
		val file = new File("/home/shiba/Documents/git/redshiftSample/client/jsonData/escapes.json")
		val fileName = "escapes.json"
		putToS3(file, fileName)

		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		DB localTx { implicit session =>

			sql"DROP TABLE IF EXISTS category".execute.apply()

			sql"""
create table category (
	catid bigint,
	catgroup varchar(35),
	catname varchar(35),
	catdesc varchar(200)
)
""".execute.apply()

			copy(fileName, "category")

			val memberId = sql"select catgroup from category where catid = 1" // don't worry, prevents SQL injection
				.map(rs => rs.string("catgroup")) // extracts values from rich java.sql.ResultSet
				.single // single, list, traversable
				.apply() // Side effect!!! runs the SQL using Connection

			println(memberId)
		}
	}


}