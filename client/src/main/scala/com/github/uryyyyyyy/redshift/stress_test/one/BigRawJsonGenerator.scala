package com.github.uryyyyyyy.redshift.stress_test.one

import java.io.PrintWriter

import com.github.uryyyyyyy.redshift.Util
import com.github.uryyyyyyy.redshift.stress_test.util.Utility
import com.typesafe.config.ConfigFactory

object BigRawJsonGenerator {

	def main(args: Array[String]) {
		Util.timeCounter(execute)
	}

	def execute() {
		create(10000000, 1)
	}

	def createJsonFile(size: Int, i: Int): Unit = {
		val writer = new PrintWriter(s"./jsonData/one/${i}.json")

		(1 to size).toStream
			.map(v => Utility.createOneJson(size * (i-1) + v))
			.foreach(v => {
				writer.write(v)
				writer.write("\n")
			})

		writer.close()
	}

	def create(size: Int, count: Int): Unit = {

		(1 to count).toStream
			.foreach(i => createJsonFile(size, i))
	}

}