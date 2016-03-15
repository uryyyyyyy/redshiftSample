name := """redshiftSampleClient"""

version := "1.0"

scalaVersion := "2.11.7"

val redshiftVersion = "1.1.10.1010"
val redshiftUrl = s"https://s3.amazonaws.com/redshift-downloads/drivers/RedshiftJDBC41-$redshiftVersion.jar"

libraryDependencies ++= Seq(
	"org.scalikejdbc" %% "scalikejdbc" % "2.3.5",
	"com.amazonaws" % "aws-java-sdk-redshift" % "1.10.60",
	"ch.qos.logback" % "logback-classic" % "1.1.6",
	"com.h2database" % "h2" % "1.4.191",
	"com.amazonaws" % "redshift.jdbc" % redshiftVersion from redshiftUrl,
	"com.typesafe" % "config" % "1.3.0"
)

