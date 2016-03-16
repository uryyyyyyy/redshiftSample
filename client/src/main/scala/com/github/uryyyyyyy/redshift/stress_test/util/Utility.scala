package com.github.uryyyyyyy.redshift.stress_test.util

import com.github.uryyyyyyy.redshift.Tables
import scalikejdbc.{DBSession, _}

object Utility {

	def createOneJson(v: Int): String = {
		s"""{"catdesc": "${v}Major League Baseball","catid": ${v},"catgroup": "Sports","catname": "MLB"}""".stripMargin
	}

	def createSalesJson(v: Int): String = {
		s"""{"salesid": ${v},"listid": ${v},"sellerid": 1,"buyerid": 10,"eventid": 10000,"dateid": 10,"qtysold": 10,"pricepaid": 1000,"commission": 1000, "saletime": "2008-02-15 04:05:00 PM"}""".stripMargin
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