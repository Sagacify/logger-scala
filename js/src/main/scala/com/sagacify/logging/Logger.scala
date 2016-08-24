package com.sagacify.logging

import scala.scalajs.js

import js.annotation.JSExport
import js.Dynamic.{global => g}

@JSExport("logger")
class Logger(val module: String) extends LoggerT[js.Any] {

  val companion = LoggerCompanion

  @JSExport
  def fatal(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.FATAL) {
      log(Level.FATAL, event, data, meta)
    }

  @JSExport
  def error(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.ERROR) {
      log(Level.ERROR, event, data, meta)
    }

  @JSExport
  def warn(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.WARN) {
      log(Level.WARN, event, data, meta)
    }

  @JSExport
  def info(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.INFO) {
      log(Level.INFO, event, data, meta)
    }

  @JSExport
  def debug(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.DEBUG) {
      log(Level.DEBUG, event, data, meta)
    }

  @JSExport
  def trace(event: String, data: js.Any, meta: js.Any): Unit =
    if (LoggerCompanion.level <= Level.DEBUG) {
      log(Level.DEBUG, event, data, meta)
    }
}
