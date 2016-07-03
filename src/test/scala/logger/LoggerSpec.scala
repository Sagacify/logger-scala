package logger

import Converter.extract
import Converter.error2JsonInput
import Converter.map2JValue
// import org.json4s.JsonDSL._
import org.scalatest.FlatSpec

case class Poney(name: String, age: Int)

class LoggerSpec extends FlatSpec  {

  "A logger" should "be able to serialize errors" in {
    val l = new Logger("test")
    val e = new java.lang.Exception("Excetpion test")
    l.error("test", e)
  }

  it should "be able to serialize a Map " in {
    val l = new Logger("test")
    l.fatal("test", Map("test" -> 5)) // age mental hein!
  }

  it should "be able to serialize case classes" in {
    val l = new Logger("test")
    l.fatal("test", extract(Poney("Louis", 5))) // age mental hein!
  }
}
