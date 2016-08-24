# Scala-logger

This branch is POC(proof of concept) as to the feasability of creating a shared library usable in scala/jvm, scala/js and callable from javascript.

The base logger implementation can be found in the master branch of this repo for scala and [here](https://github.com/sagacify/logger) for javascript.

The logger uses upickle and compile time macros to check that all object that the logger will serialize are indeed serializable and will automatically generate serializer for case class. It also allows serializing Maps of [String, Any] and Seq[Any] as long as the Any doesn't contain any non-primitive values (no case classes unfortunately).

For performance reasons, macros are used to garantee that objects are serialized and functions evaluated only if the log level requires it.

Javascript exported functions unfortunately cannot be optimised that way, but the native js objects that are passed are never evaluated by scala code. They are directly placed into a js.Dynamic object that is serialized using the native JSON.serialize.

The final result is not too bad and can be tested here.

For usage, check the tests. shared/src/test/scala for scala/{JVM/JS} and test for javascript.

To build:
```
sbt test package
npm build
npm test
```
We have decided not to use this logger in the end as the gains of having a common library for the two languages currently do not outweight the costs in {implementation, packaging, maintenance, performance}.
Feel free to fork the project and PM us if you wish to have the code in a more permissive license.
