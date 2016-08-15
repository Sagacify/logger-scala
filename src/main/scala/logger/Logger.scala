package com.sagacify.logging

import scala.util.Properties
import scala.language.experimental.macros

import upickle.Js.Value
import upickle.Js.Obj
import upickle.Js.Str
import upickle.Js.Num
import upickle.Js.Null
import upickle.json.write

import Converter.error2JsonInput

class Logger(val module: String) {

  final def log(
      level: Int,
      event: String,
      data: Value = Null,
      meta: Value = Null): Unit =
    Logger.log(level, module, event, data, meta)

  def fatal(event: String): Unit = macro Macro.log1
  def fatal(event: String, data: Value): Unit = macro Macro.log2
  def fatal(event: String, data: Value, meta: Value): Unit = macro Macro.log3

  def error(event: String): Unit = macro Macro.log1
  def error(event: String, data: Value): Unit = macro Macro.log2
  def error(event: String, data: Value, meta: Value): Unit = macro Macro.log3

  def warn(event: String): Unit = macro Macro.log1
  def warn(event: String, data: Value): Unit = macro Macro.log2
  def warn(event: String, data: Value, meta: Value): Unit = macro Macro.log3

  def info(event: String): Unit = macro Macro.log1
  def info(event: String, data: Value): Unit = macro Macro.log2
  def info(event: String, data: Value, meta: Value): Unit = macro Macro.log3

  def debug(event: String): Unit = macro Macro.log1
  def debug(event: String, data: Value): Unit = macro Macro.log2
  def debug(event: String, data: Value, meta: Value): Unit = macro Macro.log3

  def trace(event: String): Unit = macro Macro.log1
  def trace(event: String, data: Value): Unit = macro Macro.log2
  def trace(event: String, data: Value, meta: Value): Unit = macro Macro.log3

}

object Logger {

  def getNameAndVersion: (String, String) = {
    val stack = Thread.currentThread.getStackTrace
    val mainStackElement = stack(stack.length - 1)
    val mainClass = Class.forName(mainStackElement.getClassName())
    (mainClass.getSimpleName, mainClass.getPackage.getImplementationVersion)
  }

  def getHostName: String = java.net.InetAddress.getLocalHost.getHostName()

  val (name, version) = getNameAndVersion
  val hostname = getHostName


  val level = try {
    Properties.envOrNone("LOG_LEVEL").map(Level.apply).getOrElse(Level.INFO)
  } catch {
    case e: Throwable => {
      log(Level.FATAL, "Logger", "LOG_LEVEL_FAIL", e)
      throw e
    }
  }

  private val coreFields: List[(String, Value)] = List(
    ("name", Str(name)),
    ("version", Str(version)),
    ("hostname", Str(hostname))
  )

  final def log(
      level: Int,
      module: String,
      event: String,
      data: Value = Null,
      meta: Value = Null): Unit =
    println(
      write(
        Obj(
          (List(
            ("event", Str(event)),
            ("data", data),
            ("meta", meta),
            ("time", Str(java.time.Instant.now().toString())),
            ("module", Str(module)),
            ("level", Num(level))
          ) ++ coreFields): _*
        )
      )
    )
}
