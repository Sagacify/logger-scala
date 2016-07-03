package logger

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

import org.json4s.jackson.JsonMethods.compact
import org.json4s.JsonAST.JField
import org.json4s.JsonAST.JInt
import org.json4s.JsonAST.JNull
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JString
import org.json4s.JsonAST.JValue

object Level {
    val FATAL = 60
    val ERROR = 50
    val WARN = 40
    val INFO = 30
    val DEBUG = 20
    val TRACE = 10
}

class Logger(module: String) {

  private val tz = TimeZone.getTimeZone("UTC")
  private val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'")
  df.setTimeZone(tz)

  private val coreFields : List[JField] = List(
    JField("name", JString(getClass.getPackage.getName)),
    JField("version", JString(getClass.getPackage.getImplementationVersion)),
    JField("module", JString(module)),
    JField("hostname", JString(java.net.InetAddress.getLocalHost.getHostName()))
  )

  private def log(level: Int, fields: List[JField]): Unit = {
    println(
      compact(
        JObject(coreFields ++ List(
          JField("level", JInt(level)),
          JField("time", JString(df.format(new Date())))
        ) ++ fields)
      )
    )
  }

  private def asFields(event: String, data: JValue, meta: JValue
      ): List[JField] =
    List(
      JField("event", JString(event)),
      JField("data", data),
      JField("meta", meta)
    )

  final def fatal(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.FATAL, asFields(event, data, meta))
  }

  final def error(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.ERROR, asFields(event, data, meta))
  }

  final def warn(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.WARN, asFields(event, data, meta))
  }

  final def info(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.INFO, asFields(event, data, meta))
  }

  final def debug(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.DEBUG, asFields(event, data, meta))
  }

  final def trace(
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit = {
    log(Level.TRACE, asFields(event, data, meta))
  }
}

