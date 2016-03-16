package com.github.uryyyyyyy.redshift.stress_test.generator

import java.io.{File, PrintWriter}

import com.github.uryyyyyyy.redshift.Util
import com.github.uryyyyyyy.redshift.stress_test.util.Utility
import scalikejdbc.DBSession

object JsonUploader {

	def main(args: Array[String]) {
		Util.timeCounter(execute)
	}

	def execute() {
		upload(1)
	}

	def upload(count: Int): Unit ={
		val file = new File(s"/home/shiba/Documents/git/redshiftSample/client/jsonData/sales/one/${count}.json")
		val fileName = s"${count}.json"
		Util.putToS3(file, fileName, "sales/one/")
	}

}