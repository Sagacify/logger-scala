package com.sagacify.logging

import scala.util.Properties

import scala.scalajs.js
import scala.scalajs.js.Any
import scala.scalajs.js.Dynamic.global


object LoggerCompanion extends Companion[js.Any]{

  val level: Int = Level(
    js.eval("process.env.LOG_LEVEL || 'INFO'").toString)

  def getNameAndVersion: (String, String) = {
    try {
      val pkg = global.require(global.process.env.PWD + "/package.json")
      (pkg.name.toString, pkg.version.toString)
    } catch {
      case e: Throwable => {
        println("FAILED to get pkg " +
          f"${global.process.env.PWD + "/package.json"} ${e.getMessage}")
        ("NO_NAME", "NO_VERSION")
      }
    }
  }

  val (name, version) = getNameAndVersion

  val hostname = try {
    val os = global.require("os")
    os.hostname().toString()
  } catch {
    case e: Exception => {
      println(f"FAILED to get hostname ${e.getMessage}")
      "NO_HOSTNAME"
    }
  }

  def time: String = new js.Date().toISOString()

  def undefined: js.Any = js.undefined

  override def log(
      // module specific
      module    : String,
      // log specific
      level     : Int,
      event     : String,
      data      : js.Any,
      meta      : js.Any,
      // Global
      time      : String = LoggerCompanion.time,
      name      : String = LoggerCompanion.name,
      version   : String = LoggerCompanion.version,
      hostname  : String = LoggerCompanion.hostname): String =
    js.JSON.stringify(
      js.Dynamic.literal(
        level     = level,
        event     = event,
        data      = data,
        meta      = meta,
        time      = time,
        module    = module,
        name      = name,
        version   = version,
        hostname  = hostname
      )
    )
}

