package logger

import org.scalatest.FlatSpec

import Converter.extract
import Converter.error2JsonInput
import Converter.map2JValue

case class Poney(name: String, age: Int)

class LoggerSpec extends FlatSpec  {

  def lazyString(): String = {
    throw new java.lang.Exception("This should not have been evaluated.")
    "Yipee!"
  }

  "A logger" should "lazy evaluate what it logs" in {
    val l = new Logger("test")
    l.trace(lazyString)

    intercept[Exception] {
      l.warn(lazyString)
    }
  }

  it should "be able to serialize errors" in {
    val l = new Logger("test")
    val e = new java.lang.Exception("Excetpion test")
    l.warn("test", e)
  }

  it should "be able to serialize a Map " in {
    val l = new Logger("test")
    l.warn("test", Map("test" -> 5)) // age mental hein!
  }

  it should "be able to serialize case classes" in {
    val l = new Logger("test")
    l.warn("test", extract(Poney(name="Louis", age=5))) // age mental hein!
  }
}
