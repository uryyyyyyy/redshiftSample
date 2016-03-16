package com.github.uryyyyyyy.redshift

import scalikejdbc._

object Tables {

	def recreateTable(implicit session:DBSession): Unit ={

		sql"DROP TABLE IF EXISTS members".execute.apply()

		sql"""
create table members (
  id BIGINT IDENTITY PRIMARY KEY,
  name varchar(64) not null,
  created_at timestamp not null
)
""".execute.apply()
	}

	def recreateCategoryTable(implicit session:DBSession): Unit ={
		sql"DROP TABLE IF EXISTS category".execute.apply()

		sql"""
create table category (
	catid bigint,
	catgroup varchar(35),
	catname varchar(35),
	catdesc varchar(200)
)
""".execute.apply()
	}

}
