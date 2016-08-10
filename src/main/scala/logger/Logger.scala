package logger

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

import scala.util.Properties
import scala.language.experimental.macros

import org.json4s.jackson.JsonMethods.compact
import org.json4s.JsonAST.JField
import org.json4s.JsonAST.JInt
import org.json4s.JsonAST.JNull
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JString
import org.json4s.JsonAST.JValue

import Converter.error2JsonInput

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
    case e: java.util.NoSuchElementException => {
      throw new java.lang.Error(f"Neither log level nor int: $level")
    }
    case e: java.lang.AssertionError => {
      throw new java.lang.Error(f"Log level must be positive: $level")
    }
  }

  final val FATAL = values("FATAL")
  final val ERROR = values("ERROR")
  final val WARN = values("WARN")
  final val INFO = values("INFO")
  final val DEBUG = values("DEBUG")
  final val TRACE = values("TRACE")
}

class Logger(module: String) {

  final def log(level: Int, event: String, data: JValue, meta: JValue): Unit =
    Logger.log(level, module, event, data, meta)

  def fatal(event: String): Unit = macro LoggerMacro.log1
  def fatal(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def fatal(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

  def error(event: String): Unit = macro LoggerMacro.log1
  def error(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def error(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

  def warn(event: String): Unit = macro LoggerMacro.log1
  def warn(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def warn(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

  def info(event: String): Unit = macro LoggerMacro.log1
  def info(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def info(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

  def debug(event: String): Unit = macro LoggerMacro.log1
  def debug(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def debug(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

  def trace(event: String): Unit = macro LoggerMacro.log1
  def trace(event: String, data: JValue): Unit = macro LoggerMacro.log2
  def trace(event: String, data: JValue, meta: JValue): Unit =
      macro LoggerMacro.log3

}

object Logger {

  def getNameAndVersion: (String, String) = {
    val stack = Thread.currentThread.getStackTrace
    val mainStackElement = stack(stack.length - 1)
    val mainClass = Class.forName(mainStackElement.getClassName())
    (mainClass.getSimpleName, mainClass.getPackage.getImplementationVersion)
  }

  val (name, version) = getNameAndVersion

  private val tz = TimeZone.getTimeZone("UTC")
  private val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'")
  df.setTimeZone(tz)

  val level = try {
    Properties.envOrNone("LOG_LEVEL").map(Level.apply).getOrElse(Level.INFO)
  } catch {
    case e: Throwable => {
      log(Level.FATAL, "Logger", "LOG_LEVEL_FAIL", e)
      throw e
    }
  }

  private val coreFields: List[JField] = List(
    JField("name", JString(name)),
    JField("version", JString(version)),
    JField("hostname", JString(java.net.InetAddress.getLocalHost.getHostName()))
  )

  final def log(
      level: Int,
      module: String,
      event: String,
      data: JValue = JNull,
      meta: JValue = JNull): Unit =
    println(compact(JObject(List(
        JField("event", JString(event)),
        JField("data", data),
        JField("meta", meta),
        JField("time", JString(df.format(new Date()))),
        JField("module", JString(module)),
        JField("level", JInt(level))
      ) ++ coreFields)))

}
