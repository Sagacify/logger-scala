package com.sagacify.logging

import java.io.PrintStream
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

import org.scalatest.FlatSpec

import com.sagacify.logging._

import microjson._
import microjson.Json

case class Poney(name: String, age: Int)

class LoggerSpec extends FlatSpec {

  val l = new Logger("LoggerSpec")

  def getLog(test: () => Unit): Map[String, JsValue] = {
    // this bypasses the stdout in the tests and makes it available
    val out = new ByteArrayOutputStream()
    val psOut = new PrintStream(out)

    try {
      Console.withOut(psOut) { test() }
      Json.read(out.toString).value.asInstanceOf[Map[String, JsValue]]
    }
    finally {
      out.close
      psOut.close
    }
  }

  val log1 = getLog(() => l.info("test"))
  val log2 = getLog(() => l.info("test", "data"))
  val log3 = getLog(() => l.info("test", "data", "meta"))
  val logs = Seq(log1, log2, log3)

  "A logger" should "have all core fields" in {
    // Cheks ISO date
    val pattern = Pattern.compile(
      "20\\d\\d-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\dZ")

    logs.foreach{ log =>
      assert(log("module") match
        { case JsString("LoggerSpec") => true; case _ => false})
      assert(log("level") match
        { case JsNumber("30") => true; case _ => false})
      assert(log("time") match{
        case JsString(time) => {
          val matcher = pattern.matcher(time)
          matcher.find()
        }
        case _ => false
      })
      assert(log("event") match
        { case JsString(_) => true; case _ => false})
      assert(log("name") match
        { case JsString(_) => true; case _ => false})
      assert(log("version") match
        { case JsString(_) => true; case _ => false})
      assert(log("hostname") match
        { case JsString(_) => true; case _ => false})
    }
  }

  it should "not serialize null values" in {
    assert(log1.get("data").isEmpty)
    assert(log1.get("meta").isEmpty)
    assert(log2.get("meta").isEmpty)
  }


  def getData(json: Map[String, JsValue]): Map[String, JsValue] =
    json("data")
      .asInstanceOf[JsObject].value
      .asInstanceOf[Map[String, JsValue]]

  def lazyString(): String = {
    throw new Exception("This should not have been evaluated.")
    "Yipee!"
  }

  it should "lazy evaluate what it logs" in {
    l.trace(lazyString)

    intercept[Exception] {
      l.warn(lazyString)
    }
  }

  it should "be able to serialize errors" in {
    val message = "Excetpion test"
    val data = getData(getLog(() => l.warn("test", new Exception(message))))

    assert(data("message") match
      { case JsString(message) => true; case _ => false})
    assert(data("name") match
      { case JsString("java.lang.Exception") => true; case _ => false})
    assert(data("stack") match
      { case JsString(_) => true; case _ => false})
  }

  it should "be able to serialize a simple Map " in {

    val data = getData(getLog(() => l.warn("test", Map("test" -> 5))))

    assert(data("test") match
      { case JsNumber("5") => true; case _ => false})
  }

  it should "be able to serialize a complex Map " in {
    val data = getData(
      getLog(() => l.warn("test", Map("key1" -> 5, "key2" -> "string"))))

    assert(data("key1") match
      { case JsNumber("5") => true; case _ => false})
    assert(data("key2") match
      { case JsString("string") => true; case _ => false})
  }

  it should "be able to serialize a single type array" in {
    val map = getLog(() => l.warn("test", Seq(0, 1, 2, 3)))

    assert(map("data") match {
      case JsArray(arr) => {
        (0 until 4).map{ i =>
          val si = f"$i"
          (arr(i) match {
            case JsNumber(si) => true
            case _ => false
          })
        }.reduce(_ & _)
      }
      case _ => false
      })
  }

  it should "be able to serialize multiple-type arrays" in {
    val map = getLog(() => l.warn("test", Seq(0, "1", 2, "3", 4)))

    assert(map("data") match {
      case JsArray(arr) => {
        (0 until 4).map{ i =>
          if ((i % 2) == 0) {
            val si = f"$i"
            (arr(i) match {
              case JsNumber(si) => true
              case _ => false
            })
          } else {
            val si = f"$i"
            (arr(i) match {
              case JsString(si) => true
              case _ => false
            })
          }
        }.reduce(_ & _)
      }
      case _ => false
      })
  }

  it should "be able to serialize case classes" in {
    val data = getData(getLog(() =>
      l.warn("test", Poney(name="Louis", age=8)))) // age mental hein!

    assert(data("name") match
      { case JsString("Louis") => true; case _ => false})
    assert(data("age") match
      { case JsNumber("8") => true; case _ => false})
  }
}
