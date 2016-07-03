name := "logger"

version := "0.1"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-feature", "-Ywarn-unused-import", "-Xlint")

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "latest.integration",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
