package com.acme

import org.scalatest.FlatSpec

import com.sagacify.logging.Logging


class LoggingSpec extends FlatSpec with Logging {

  "A logging class" should "log under its own name" in {
    assert(log.module == "LoggingSpec")
  }
}
