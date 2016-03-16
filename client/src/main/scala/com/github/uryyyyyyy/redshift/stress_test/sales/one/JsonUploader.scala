package com.github.uryyyyyyy.redshift.stress_test.sales.one

import java.io.File

import com.github.uryyyyyyy.redshift.Util

object JsonUploader {

	def main(args: Array[String]) {
		Util.timeCounter(execute)
	}

	def execute() {
		upload(1)
	}

	def upload(count: Int): Unit ={
		val file = new File(s"./jsonData/sales/one/${count}.json")
		val fileName = s"${count}.json"
		Util.putToS3(file, fileName, "redshift/sales/one/")
	}

}