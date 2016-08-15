package com.sagacify.logging

import scala.language.implicitConversions

import java.io.StringWriter
import java.io.PrintWriter

import upickle.Js.Arr
import upickle.Js.False
import upickle.Js.Null
import upickle.Js.Num
import upickle.Js.Obj
import upickle.Js.Str
import upickle.Js.True
import upickle.Js.Value

object Converter {

  def any2value(value: Any): Value = value match {
      case x: Value           => x
      case x: Byte            => Num(x)
      case x: Short           => Num(x)
      case x: Int             => Num(x)
      case x: Long            => Num(x)
      case x: Float           => Num(x)
      case x: Double          => Num(x)
      case x: String          => Str(x)
      case x: Char            => Str(x.toString)
      case x: Boolean         => if (x) True else False
      case Some(v)            => any2value(v)
      case None               => Null
      case null               => Null                     // scalastyle:ignore
      case x: Map[_, _]       => map2value(x)
      case x: Iterable[_]     => iterable2value(x)
      case e: Any             =>
        new Exception(f"Cannot serialize: ${e.getClass.getName}")
    }

  implicit def map2value(m: Map[_, _]): Obj = Obj(
    m.map{ case (key, value) =>
      (key.toString, any2value(value))
    }.toSeq: _*
  )

  implicit def iterable2value(iter: Iterable[_]): Arr = Arr(
    iter.toSeq.map(any2value): _*
  )

  implicit def error2JsonInput(e: Throwable): Value = {
    val sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw))
    Obj(
      List(
        ("message", Str(e.getMessage)),
        ("name", Str(e.getClass.getName)),
        ("stack", Str(sw.toString))
      ): _*
    )
  }
}
