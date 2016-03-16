package com.github.uryyyyyyy.redshift.stress_test.ten

import java.io.File

import com.github.uryyyyyyy.redshift.Util
import com.github.uryyyyyyy.redshift.stress_test.util.Utility
import com.typesafe.config.ConfigFactory
import scalikejdbc.{SQL, ConnectionPool, DB, DBSession}

object BulkUploader1 {

	val config = ConfigFactory.load()

	def main(args: Array[String]) {
		Util.timeCounter(execute)
	}

	def execute() {

		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		DB.localTx { implicit session =>
			Utility.setup
			val sql = s"""
				           |COPY category FROM 's3://${config.getString("s3Bucket")}/redshift/'
				           |CREDENTIALS 'aws_access_key_id=${config.getString("awsAccessKey")};aws_secret_access_key=${config.getString("awsSecretKey")}'
				           |json 'auto' gzip
      """.stripMargin
			SQL(sql).execute.apply()
		}
		//			check
	}

	def upload(count: Int)(implicit session:DBSession): Unit ={
//		val file = new File(s"/home/shiba/Documents/git/redshiftSample/client/jsonData/ten/${count}.json")
//		val fileName = s"sample_${count}.json"
//		Util.putToS3(file, fileName)
	}

}
