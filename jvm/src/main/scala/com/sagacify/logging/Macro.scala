package com.sagacify.logging

import scala.reflect.macros.blackbox.Context

import upickle.Js.Value

private object Macro {

  def log1(c: Context)(event: c.Expr[String]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.LoggerCompanion.level <= $level) {
      ${c.prefix}.log($level, $event)
    }
    """
  }

  def log2[A](c: Context)(event: c.Expr[String], data: c.Expr[A]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.LoggerCompanion.level <= $level) {
      ${c.prefix}.log($level, $event, upickle.default.writeJs($data))
    }
    """
  }

  def log3[A, B](c: Context)(
      event: c.Expr[String], data: c.Expr[A], meta: c.Expr[B]) = {
    import c.universe._ // scalastyle:ignore

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    q"""
    if (com.sagacify.logging.LoggerCompanion.level <= $level) {
      ${c.prefix}.log(
        $level,
        $event,
        upickle.default.writeJs($data),
        upickle.default.writeJs($meta))
    }
    """
  }
}
