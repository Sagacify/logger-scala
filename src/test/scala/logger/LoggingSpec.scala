package com.acme

import org.scalatest.FlatSpec

import logger.Logging

case class Poney(name: String, age: Int)

class LoggingSpec extends FlatSpec with Logging {

  "A logging class" should "log under its own name" in {
    assert(log.module == "LoggingSpec")
  }
}
