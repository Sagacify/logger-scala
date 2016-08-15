scalaVersion in ThisBuild := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-feature", "-Ywarn-unused-import", "-Xlint")

scalaJSStage in Global := FastOptStage

lazy val root = project.in(file(".")).
  aggregate(loggerJS, loggerJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val logger = crossProject.in(file(".")).
  settings(
    name := "logger",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scala-lang"   % "scala-reflect"  % scalaVersion.value,
      "com.lihaoyi" %%% "upickle" % "0.4.1",
      "org.scalatest" %%% "scalatest"      % "3.0.0" % "test"
    )
  ).
  jvmSettings(
    // Add JVM-specific settings here
  ).
  jsSettings(
    // Add JS-specific settings here
    scalaJSUseRhino in Global := false
  )

lazy val loggerJVM = logger.jvm
lazy val loggerJS = logger.js
