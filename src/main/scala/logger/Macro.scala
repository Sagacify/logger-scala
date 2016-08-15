package com.sagacify.logging

import scala.reflect.macros.blackbox.Context

import upickle.Js.Value

private object Macro {

  def log1(c: Context)(event: c.Expr[String]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.Logger.level <= $level) {
      ${c.prefix}.log($level, $event)
    }
    """
  }

  def log2(c: Context)(event: c.Expr[String], data: c.Expr[Value]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.Logger.level <= $level) {
      ${c.prefix}.log($level, $event, $data)
    }
    """
  }

  def log3(c: Context)(
      event: c.Expr[String], data: c.Expr[Value], meta: c.Expr[Value]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.Logger.level <= $level) {
      ${c.prefix}.log($level, $event, $data, $meta)
    }
    """
  }
}
