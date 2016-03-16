package com.github.uryyyyyyy.redshift.stress_test.ten

import java.io.File
import java.util.concurrent.Executors

import com.github.uryyyyyyy.redshift.Util
import com.github.uryyyyyyy.redshift.stress_test.util.Utility
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
		val settings = ConnectionPoolSettings(initialSize = 10, maxSize = 10)
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"), settings)

		val executors = Executors.newFixedThreadPool(10)

		implicit val s = ExecutionContext.fromExecutorService(executors)

		DB.localTx { implicit session =>
			Utility.setup
		}

		val fList = (1 to 10).toStream
			.map(v => {
				Future {
					DB.localTx { implicit session =>
						println("start " + v)
						upload(v)
						println("finish " + v)
					}
				}
			})
		val ss = Future.sequence(fList)
		Await.result(ss, Duration.Inf)
		//			check
	}

	def upload(count: Int)(implicit session:DBSession): Unit ={
		val file = new File(s"/home/shiba/Documents/git/redshiftSample/client/jsonData/ten/${count}.json")
		val fileName = s"sample_${count}.json"
		Util.putToS3(file, fileName)
		Util.copy(fileName, "category")
	}

}
