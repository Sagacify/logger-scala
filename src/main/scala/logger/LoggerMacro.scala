package logger

import scala.reflect.macros.blackbox.Context

import org.json4s.JsonAST.JValue

private object LoggerMacro {

  type LoggerContext = Context { type PrefixType = Logger }

  def log1(c: LoggerContext)(event: c.Expr[String]) = {
    import c.universe._

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    val jnull = q"org.json4s.JsonAST.JNull"

    q"""
    if (logger.Logger.level <= $level) {
      ${c.prefix}.log($level, $event, $jnull, $jnull)
    }
    """
  }

  def log2(c: LoggerContext)(event: c.Expr[String], data: c.Expr[JValue]) = {
    import c.universe._

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    val jnull = q"org.json4s.JsonAST.JNull"

    q"""
    if (logger.Logger.level <= $level) {
      ${c.prefix}.log($level, $event, $data, $jnull)
    }
    """
  }

  def log3(c: LoggerContext)(
      event: c.Expr[String], data: c.Expr[JValue], meta: c.Expr[JValue]) = {
    import c.universe._

    val level: Int = Level(c.macroApplication.symbol.name.toString)

    val jnull = q"org.json4s.JsonAST.JNull"

    q"""
    if (logger.Logger.level <= $level) {
      ${c.prefix}.log($level, $event, $data, $meta)
    }
    """
  }
}
