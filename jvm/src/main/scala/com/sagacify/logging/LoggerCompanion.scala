package com.sagacify.logging

import scala.util.Properties

import upickle.default.writeJs
import upickle.Js
import upickle.json.write

object LoggerCompanion extends Companion[Js.Value]   {

  val level: Int = try {
    Properties.envOrNone("LOG_LEVEL").map(Level.apply).getOrElse(Level.INFO)
  } catch {
    case e: Throwable => {
      log("Logger", Level.FATAL, "LOG_LEVEL_FAIL", writeJs(e))
      throw e
    }
  }

  def getNameAndVersion: (String, String) = {
    val stack = Thread.currentThread.getStackTrace
    val mainStackElement = stack(stack.length - 1)
    val mainClass = Class.forName(mainStackElement.getClassName)
    (mainClass.getSimpleName, mainClass.getPackage.getImplementationVersion)
  }

  lazy val (name, version) = getNameAndVersion
  lazy val hostname: String = java.net.InetAddress.getLocalHost.getHostName
  def time: String = java.time.Instant.now.toString

  def undefined: Js.Value = Js.Null

  override def log(
      // module specific
      module    : String,
      // log specific
      level     : Int,
      event     : String,
      data      : Js.Value = Js.Null,
      meta      : Js.Value = Js.Null,
      // Global
      time      : String = LoggerCompanion.time,
      name      : String = LoggerCompanion.name,
      version   : String = LoggerCompanion.version,
      hostname  : String = LoggerCompanion.hostname): String =
    write(
     Js.Obj(
       List(
         ("level",     Js.Num(level)),
         ("event",     Js.Str(event)),
         ("data",      data),
         ("meta",      meta),
         ("time",      Js.Str(time)),
         ("module",    Js.Str(module)),
         ("name",      Js.Str(name)),
         ("version",   Js.Str(version)),
         ("hostname",  Js.Str(hostname))
       ): _*
     )
   )
}
