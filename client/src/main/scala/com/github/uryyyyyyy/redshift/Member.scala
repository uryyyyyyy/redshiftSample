package com.github.uryyyyyyy.redshift

import org.joda.time._
import scalikejdbc._

case class Member(id: Long, name: Option[String], createdAt: DateTime)

object Member extends SQLSyntaxSupport[Member] {
	override val tableName = "members"
	def apply(rs: WrappedResultSet): Member = new Member(
		rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at"))
}