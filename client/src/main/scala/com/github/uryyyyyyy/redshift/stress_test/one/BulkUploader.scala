package com.github.uryyyyyyy.redshift.stress_test.one

import java.io.File
import java.util.concurrent.Executors

import com.github.uryyyyyyy.redshift.stress_test.util.Utility
import com.github.uryyyyyyy.redshift.{Tables, Util}
import com.typesafe.config.ConfigFactory
import scalikejdbc.{ConnectionPool, DB, DBSession, _}

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, _}

object BulkUploader {

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
		val file = new File(s"/home/shiba/Documents/git/redshiftSample/client/jsonData/one/${count}.json")
		val fileName = s"sample_${count}.json"
		Util.putToS3(file, fileName,"")
		Util.copy(fileName, "category")
	}

}
