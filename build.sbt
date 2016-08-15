name := "logger"

version := "0.1"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-feature", "-Ywarn-unused-import", "-Xlint")

libraryDependencies ++= Seq(
  "com.lihaoyi"     %% "upickle"        % "0.4.1",
  "org.scala-lang"   % "scala-reflect"  % scalaVersion.value    % "provided",
  "org.scalatest"   %% "scalatest"      % "2.2.6"               % "test"
)
