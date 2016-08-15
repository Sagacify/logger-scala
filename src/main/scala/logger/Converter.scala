package com.sagacify.logging

import scala.language.implicitConversions

import java.io.StringWriter
import java.io.PrintWriter

import org.json4s.DefaultFormats
import org.json4s.Extraction
import org.json4s.JsonAST.JField
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JString
import org.json4s.JsonAST.JValue
import org.json4s.JsonWriter

object Converter {
  implicit val formats = DefaultFormats

  def extract(a: Any): JValue = {
    Extraction.decomposeWithBuilder(a, JsonWriter.ast)
  }

  implicit def map2JValue(m: Map[String, _]): JValue = {
    Extraction.decomposeWithBuilder(m, JsonWriter.ast)
  }

  implicit def error2JsonInput(e: Throwable): JValue = {
    val sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw))
    JObject(
      List(
        JField("message", JString(e.getMessage)),
        JField("name", JString(e.getClass.getName)),
        JField("stack", JString(sw.toString))
      )
    )
  }
}
