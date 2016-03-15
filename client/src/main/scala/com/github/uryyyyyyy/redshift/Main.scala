package com.github.uryyyyyyy.redshift

import com.typesafe.config.ConfigFactory
import scalikejdbc._
object Main {

	def main(args: Array[String]) {

		val config = ConfigFactory.load()

		// initialize JDBC driver & connection pool
		Class.forName(config.getString("driverClass"))
		ConnectionPool.singleton(config.getString("dbUrl"), config.getString("dbUser"), config.getString("dbPass"))

		val name = "Alice"

		val effect = DB localTx  { implicit session =>

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

}