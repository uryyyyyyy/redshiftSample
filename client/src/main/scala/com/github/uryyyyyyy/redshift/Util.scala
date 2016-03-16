package com.github.uryyyyyyy.redshift

import java.io.{ByteArrayOutputStream, ByteArrayInputStream, File}
import java.nio.file.{Paths, Files}
import java.util.zip.GZIPOutputStream

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, ObjectMetadata}
import com.typesafe.config.ConfigFactory
import scalikejdbc.{DBSession, SQL}

object Util {

	val config = ConfigFactory.load()

	def copy(key:String, table:String)(implicit session:DBSession): Unit ={

		val sql = s"""
			           |COPY ${table} FROM 's3://${config.getString("s3Bucket")}/redshift/${key}'
			           |CREDENTIALS 'aws_access_key_id=${config.getString("awsAccessKey")};aws_secret_access_key=${config.getString("awsSecretKey")}'
			           |json 'auto' gzip
      """.stripMargin
		SQL(sql).execute.apply()
	}

	def putToS3(file:File, fileName: String, path:String): Unit ={
		val credentials = new BasicAWSCredentials(config.getString("awsAccessKey"), config.getString("awsSecretKey"))
		val s3client = new AmazonS3Client(credentials)
		val gzippedFile = gzipFile(file)

		val metadata = new ObjectMetadata()
		metadata.setContentType("text/plane")
		metadata.setContentEncoding("gzip")
		metadata.setContentLength(gzippedFile.length)

		val byte = new ByteArrayInputStream(gzippedFile)
		s3client.putObject(new PutObjectRequest(config.getString("s3Bucket"), path + fileName, byte, metadata))
	}

	def gzipFile(file:File):Array[Byte] = {
		val bytes = Files.readAllBytes(Paths.get(file.getPath))
		val baos = new ByteArrayOutputStream()
		val out = new GZIPOutputStream(baos)

		out.write(bytes, 0, bytes.length)
		out.finish()
		baos.toByteArray
	}

	def timeCounter(f: ()=> Unit):Unit = {
		val now = System.currentTimeMillis()
		f()
		val millis = System.currentTimeMillis() - now
		println("%d milliseconds".format(millis))
	}

}
