package com.github.uryyyyyyy.redshift.stress_test.sales.one

import com.github.uryyyyyyy.redshift.Util
import com.github.uryyyyyyy.redshift.stress_test.util.Utility
import com.typesafe.config.ConfigFactory
import scalikejdbc.{ConnectionPool, DB, DBSession, SQL}

object RedshiftCopyer {

	val config = ConfigFactory.load()

	def main(args: Array[String]) {
		Util.timeCounter(execute)
	}

	def execute() {

		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		DB localTx { implicit session =>
			Utility.setup
			(1 to 1).toStream
				.foreach(v => upload(v))
			//			check
		}
	}

	def upload(count: Int)(implicit session:DBSession): Unit ={
		val sql = s"""
								 								 			           |COPY sales FROM 's3://${config.getString("s3Bucket")}/redshift/sales/one/${count}.json'
								 								 			           |CREDENTIALS 'aws_access_key_id=${config.getString("awsAccessKey")};aws_secret_access_key=${config.getString("awsSecretKey")}'
								 								 			           |json 'auto' gzip
      """.stripMargin
		SQL(sql).execute.apply()
	}

}
