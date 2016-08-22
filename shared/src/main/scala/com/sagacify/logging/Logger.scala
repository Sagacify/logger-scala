package com.sagacify.logging

import scala.language.experimental.macros

import upickle.Js
import upickle.Js.Null
import upickle.json.write
import upickle.default.writeJs


trait LoggerT[C] {

  def module: String

  def companion: Companion[C]

  def log(level: Int, event: String): Unit = {
    println(companion.log(module, level, event))
  }

  def log(level: Int, event: String, data: C): Unit = {
    println(companion.log(module, level, event, data))
  }

  def log(level: Int, event: String, data: C, meta: C): Unit = {
    println(companion.log(module, level, event, data, meta))
  }

  def fatal(event: String): Unit = macro Macro.log1
  def fatal[A](event: String, data: A): Unit = macro Macro.log2[A]
  def fatal[A, B](event: String, data: A, meta: B): Unit = macro Macro.log3[A, B]

  def error(event: String): Unit = macro Macro.log1
  def error[A](event: String, data: A): Unit = macro Macro.log2[A]
  def error[A, B](event: String, data: A, meta: B): Unit = macro Macro.log3[A, B]

  def warn(event:  String): Unit = macro Macro.log1
  def warn[A](event:  String, data: A): Unit = macro Macro.log2[A]
  def warn[A, B](event:  String, data: A, meta: B): Unit = macro Macro.log3[A, B]

  def info(event:  String): Unit = macro Macro.log1
  def info[A](event:  String, data: A): Unit = macro Macro.log2[A]
  def info[A, B](event:  String, data: A, meta: B): Unit = macro Macro.log3[A, B]

  def debug(event: String): Unit = macro Macro.log1
  def debug[A](event: String, data: A): Unit = macro Macro.log2[A]
  def debug[A, B](event: String, data: A, meta: B): Unit = macro Macro.log3[A, B]

  def trace(event: String): Unit = macro Macro.log1
  def trace[A](event: String, data: A): Unit = macro Macro.log2[A]
  def trace[A, B](event: String, data: A, meta: B): Unit = macro Macro.log3[A, B]
}

trait Companion[A] {

  def undefined: A

  def log(
      // module specific
      module    : String,
      // log specific
      level     : Int,
      event     : String,
      data      : A = undefined,
      meta      : A = undefined,
      // Global
      time      : String = LoggerCompanion.time,
      name      : String = LoggerCompanion.name,
      version   : String = LoggerCompanion.version,
      hostname  : String = LoggerCompanion.hostname): String
}
