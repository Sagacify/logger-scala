package com.sagacify.logging

import java.io.PrintStream
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

import org.scalatest.FlatSpec

import upickle.default.writeJs
import com.sagacify.logging._

case class Poney(name: String, age: Int)

class LoggerSpec extends FlatSpec {

  def hideSTDOut(testCode: (ByteArrayOutputStream) => Unit): Unit = {
    // this bypasses the stdout in the tests and makes it available
    val out = new ByteArrayOutputStream()
    val psOut = new PrintStream(out)

    try {
      Console.withOut(psOut) { testCode(out) }
    }
    finally {
      out.close
      psOut.close
    }
  }

  def lazyString(): String = {
    throw new Exception("This should not have been evaluated.")
    "Yipee!"
  }

  "A logger" should "lazy evaluate what it logs" in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.trace(lazyString)

    intercept[Exception] {
      l.warn(lazyString)
    }
  }

  it should "be able to serialize errors" in hideSTDOut { (out) =>
    val l = new Logger("test")
    val e = new Exception("Excetpion test")
    l.warn("test", e)
  }

  it should "be able to serialize a simple Map " in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.warn("test", Map("test" -> 5))
  }

  it should "be able to serialize a complex Map " in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.warn("test", Map("level1" -> 5, "level1" -> "string"))
  }

  it should "be able to serialize a single type array" in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.warn("test", Seq(1, 2, 3, 4))
  }

  it should "be able to serialize multiple-type arrays" in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.warn("test", Seq(1, 2, "3", 4))
  }

  it should "be able to serialize case classes" in hideSTDOut { (out) =>
    val l = new Logger("test")
    l.warn("test", Poney(name="Louis", age=5)) // age mental hein!
  }

  it should "serialize date following ISO-8601 format" in hideSTDOut { (out) =>
    val log = new Logger("test")
    log.warn("test")
    val printed = out.toString
    val pattern = Pattern.compile("\"time\":\"20\\d\\d-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\dZ\"")
    val matcher = pattern.matcher(printed)
    assert(matcher.find())
  }
}
