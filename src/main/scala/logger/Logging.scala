package logger

trait Logging {
  protected val log: Logger = new Logger(getClass.getSimpleName)
}
