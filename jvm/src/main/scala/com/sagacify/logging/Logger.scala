package com.sagacify.logging

import upickle.Js

class Logger(val module: String) extends LoggerT[Js.Value] {
  val companion = LoggerCompanion
}
