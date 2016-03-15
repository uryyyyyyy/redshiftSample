package com.github.uryyyyyyy.redshift

import scalikejdbc._
object ScalikeJDBCSample {

	def main(args: Array[String]) {

		// initialize JDBC driver & connection pool
		Class.forName("org.h2.Driver")
		ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

		implicit val session = AutoSession

		sql"""
create table members (
  id serial not null primary key,
  name varchar(64),
  created_at timestamp not null
)
""".execute.apply()

		val name = "Alice"

		val effect = DB localTx  { implicit session =>
			sql"insert into members (name, created_at) values (${name}, current_timestamp)"
				.update.apply()
		}

		// implicit session represents java.sql.Connection
		val memberId: Option[Long] = DB readOnly { implicit session =>
			sql"select id from members where name = ${name}" // don't worry, prevents SQL injection
				.map(rs => rs.long("id")) // extracts values from rich java.sql.ResultSet
				.single // single, list, traversable
				.apply() // Side effect!!! runs the SQL using Connection
		}
		println(memberId)
	}

}