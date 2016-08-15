package com.sagacify.logging

final object Level {
  private final val values = Map(
    "FATAL" -> 60,
    "ERROR" -> 50,
    "WARN" -> 40,
    "INFO" -> 30,
    "DEBUG" -> 20,
    "TRACE" -> 10
  )

  def apply(level: String): Int = try {
    val iLevel = values.getOrElse(level.toUpperCase, level.toInt)
    assert(iLevel >= 0)
    iLevel
  } catch {
    case e: java.lang.AssertionError => {
      println(f"Log level must be positive: $level")
      0
    }
    case e: java.lang.NumberFormatException => {
      println(f"Neither log level nor int: $level")
      0
    }
  }

  final val FATAL = values("FATAL")
  final val ERROR = values("ERROR")
  final val WARN = values("WARN")
  final val INFO = values("INFO")
  final val DEBUG = values("DEBUG")
  final val TRACE = values("TRACE")
}
