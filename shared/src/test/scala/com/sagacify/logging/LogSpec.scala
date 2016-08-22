package com.sagacify.logging

import java.util.regex.Pattern

import org.scalatest.FlatSpec

class LogSpec extends FlatSpec {

  "A Logger companion" should "exist" in {
    LoggerCompanion
  }

  it should "give a date following the ISO-8601 format" in {
    val time = LoggerCompanion.time
    val pattern = Pattern.compile("20\\d\\d-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\dZ")
    val matcher = pattern.matcher(time)
    assert(matcher.find)
  }

  it should "give a name" in {
    // Works for JS, only works in package for JVM
    println(f"name: ${LoggerCompanion.name}")
  }

  it should "give a version" in {
    // Works for JS, only works in package for JVM
    println(f"version: ${LoggerCompanion.version}")
  }

  it should "give a hostname" in {
    // Works for JVM, only works in package for node
    println(f"hostname: ${LoggerCompanion.hostname}")
  }
}
