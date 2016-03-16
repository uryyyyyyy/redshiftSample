package com.github.uryyyyyyy.redshift.stress_test.util

import com.github.uryyyyyyy.redshift.Tables
import scalikejdbc.{DBSession, _}

object Utility {

	def createOneJson(v: Int): String = {
		s"""{"catdesc": "${v}Major League Baseball","catid": ${v},"catgroup": "Sports","catname": "MLB"}""".stripMargin
	}

	def setup(implicit session:DBSession): Unit ={
		Tables.recreateCategoryTable
	}

	def check(implicit session:DBSession): Unit ={
		val memberId = sql"select catgroup from category where catid = 1" // don't worry, prevents SQL injection
			.map(rs => rs.string("catgroup")) // extracts values from rich java.sql.ResultSet
			.single // single, list, traversable
			.apply() // Side effect!!! runs the SQL using Connection
		println(memberId)
	}

}