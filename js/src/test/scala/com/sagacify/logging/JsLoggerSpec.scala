package com.sagacify.logging

import java.io.PrintStream
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

import org.scalatest.FlatSpec

import scala.scalajs.js

import microjson._
import microjson.Json

import com.sagacify.logging._


class JsLoggerSpec extends FlatSpec {

  val l = new Logger("LoggerSpec")

  def getLog(test: () => Unit): String = {
    // this bypasses the stdout in the tests and makes it available
    val out = new ByteArrayOutputStream()
    val psOut = new PrintStream(out)

    try {
      Console.withOut(psOut) { test() }
      out.toString
      // Json.read(out.toString).value.asInstanceOf[Map[String, JsValue]]
    }
    finally {
      out.close
      psOut.close
    }
  }

  def getData(json: Map[String, JsValue]): Map[String, JsValue] =
    json("data")
      .asInstanceOf[JsObject].value
      .asInstanceOf[Map[String, JsValue]]

  ignore should "be able to serialize js errors" in {
    val message = "Excetpion test"

    val error: js.Any = js.Dynamic.newInstance(js.Dynamic.global.Error)(message)

    println(getLog(() => l.warn("test", error, error)))

  }


}
