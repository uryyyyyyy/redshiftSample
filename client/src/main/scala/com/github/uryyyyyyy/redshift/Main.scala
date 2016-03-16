package com.github.uryyyyyyy.redshift

import java.io.File

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

			Tables.recreateTable

			sql"insert into members (name, created_at) values (${name}, 'now')"
				.update.apply()

			val memberId = sql"select id from members where name = ${name}" // don't worry, prevents SQL injection
				.map(rs => rs.long("id")) // extracts values from rich java.sql.ResultSet
				.single // single, list, traversable
				.apply() // Side effect!!! runs the SQL using Connection

			println(memberId)
		}
	}

	def copySample(): Unit ={
		val file = new File("/home/shiba/Documents/git/redshiftSample/client/jsonData/escapes.json")
		val fileName = "escapes.json"
		Util.putToS3(file, fileName, "")

		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		DB localTx { implicit session =>

			Tables.recreateCategoryTable

			Util.copy(fileName, "category")

			val memberId = sql"select catgroup from category where catid = 1" // don't worry, prevents SQL injection
				.map(rs => rs.string("catgroup")) // extracts values from rich java.sql.ResultSet
				.single // single, list, traversable
				.apply() // Side effect!!! runs the SQL using Connection

			println(memberId)
		}
	}


}