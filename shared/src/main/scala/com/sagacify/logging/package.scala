package com.sagacify

import java.io.PrintWriter
import java.io.StringWriter

import upickle.default.writeJs
import upickle.Js
import upickle.Js.Null
import upickle.json.write


package object logging {
    def any2value(value: Any): Js.Value = value match {
      case x: Js.Value           => x
      case x: Byte            => Js.Num(x)
      case x: Short           => Js.Num(x)
      case x: Int             => Js.Num(x)
      case x: Long            => Js.Num(x)
      case x: Float           => Js.Num(x)
      case x: Double          => Js.Num(x)
      case x: String          => Js.Str(x)
      case x: Char            => Js.Str(x.toString)
      case x: Boolean         => if (x) Js.True else Js.False
      case Some(v)            => any2value(v)
      case None               => Js.Null
      case null               => Js.Null                // scalastyle:ignore
      case x: Map[_, _]       => map2Value(x)
      case x: Iterable[_]     => iterable2Value(x)
      case e: Any             =>
        throw new Exception(f"Cannot serialize: ${e.getClass.getName}")
    }

  implicit val throwable2Writer = upickle.default.Writer[java.lang.Throwable]{
    case e => {
      val sw = new StringWriter()
      e.printStackTrace(new PrintWriter(sw))
      Js.Obj(
        List(
          ("message", Js.Str(e.getMessage)),
          ("name", Js.Str(e.getClass.getName)),
          ("stack", Js.Str(sw.toString))
        ): _*
      )
    }
  }

  implicit val exception2Writer = upickle.default.Writer[java.lang.Exception]{
    case e => {
      val sw = new StringWriter()
      e.printStackTrace(new PrintWriter(sw))
      Js.Obj(
        List(
          ("message", Js.Str(e.getMessage)),
          ("name", Js.Str(e.getClass.getName)),
          ("stack", Js.Str(sw.toString))
        ): _*
      )
    }
  }

  private def map2Value(m: Map[_, _]): Js.Value =
    Js.Obj(m.map{case (key, value) => (key.toString, any2value(value))}.toSeq: _*)

  private def iterable2Value(iter: Iterable[_]): Js.Value =
    Js.Arr(iter.toSeq.map(any2value(_)): _*)

  implicit val smap2Writer = upickle.default.Writer[Map[String, _]]{
    case m => map2Value(m)
  }

  implicit val map2Writer = upickle.default.Writer[Map[_, _]]{
    case m => map2Value(m)
  }

  implicit val iterable2Writer = upickle.default.Writer[Seq[_]]{
    case iter => iterable2Value(iter)
  }
}
