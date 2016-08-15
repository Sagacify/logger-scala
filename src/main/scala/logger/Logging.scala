package com.sagacify.logging

trait Logging {
  protected val log: Logger = new Logger(getClass.getSimpleName)
}
