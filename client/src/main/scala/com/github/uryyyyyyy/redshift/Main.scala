package com.github.uryyyyyyy.redshift

import scalikejdbc._
object Main2 {

	def main(args: Array[String]) {

		// initialize JDBC driver & connection pool
		Class.forName("org.h2.Driver")
		ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

		// ad-hoc session provider on the REPL
		implicit val session = AutoSession

		// table creation, you can run DDL by using #execute as same as JDBC
		sql"""
create table members (
  id serial not null primary key,
  name varchar(64),
  created_at timestamp not null
)
""".execute.apply()

		// insert initial data
		Seq("Alice", "Bob", "Chris") foreach { name =>
			sql"insert into members (name, created_at) values (${name}, current_timestamp)".update.apply()
		}

		// for now, retrieves all data as Map value
		val entities: List[Map[String, Any]] = sql"select * from members".map(_.toMap).list.apply()

		// find all members
		val members: List[Member] = sql"select * from members".map(rs => Member(rs)).list.apply()
	}

}

import org.joda.time._

case class Member(id: Long, name: Option[String], createdAt: DateTime)

object Member extends SQLSyntaxSupport[Member] {
	override val tableName = "members"
	def apply(rs: WrappedResultSet): Member = new Member(
		rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at"))
}