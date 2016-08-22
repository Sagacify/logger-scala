package com.sagacify.logging

import scala.util.Properties

import scala.scalajs.js
import scala.scalajs.js.Any
import scala.scalajs.js.Dynamic.{global => g}


object LoggerCompanion extends Companion[js.Any]{

  val level: Int = Level(
    js.eval("process.env.LOG_LEVEL || 'INFO'").toString)

  def getNameAndVersion: (String, String) = {
    val pkg = g.require(g.process.env.PWD + "/package.json")
    (pkg.name.toString, pkg.version.toString)
  }

  val (name, version) = getNameAndVersion

  val hostname = try {
    g.os.hostname().toString()
  } catch {
    case e: Exception => {
      println(f"Attempting to get hostname ${js.Dynamic.global.global.os}")
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

