package com.acme

import org.scalatest.FlatSpec

import logger.Converter.extract
import logger.Converter.error2JsonInput
import logger.Converter.map2JValue
import logger.Logging

case class Poney(name: String, age: Int)

class LoggingSpec extends FlatSpec with Logging {

  def lazyString(): String = {
    throw new java.lang.Exception("This should not have been evaluated.")
    "Yipee!"
  }

  "A logger" should "lazy evaluate what it logs" in {
    log.trace(lazyString)

    intercept[Exception] {
      log.warn(lazyString)
    }
  }

  it should "be able to serialize errors" in {
    val e = new java.lang.Exception("Excetpion test")
    log.warn("test", e)
  }

  it should "be able to serialize a Map " in {
    log.warn("test", Map("test" -> 5)) // age mental hein!
  }

  it should "be able to serialize case classes" in {
    log.warn("test", extract(Poney(name="Louis", age=5))) // age mental hein!
  }
}
