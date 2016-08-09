# Scala-logger

This logger aims to be the scala equivalent of [saga-logger](https://github.com/Sagacify/logger).
To ensure best compatibility it emulates bunyan logging.
Exception made of:
* PID (difficult to obtain in java) cfr. [how-can-a-java-program-get-its-own-process-id](http://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id)
* msg: Bunyan logs any string to msg but as the js-logger gives an object it is never used
* v: bunyan version (we're not using bunyan)

# TODO:
## NOW
* test integration into other projects. (publish on ivy?)

## At some point
* ? Add parent logger ?
* Add support for streams
# ? Include Joda-time formats ?


# Warning
Currently only works if the logger is created in the main thread.
(It can still be used from any thread.)
Since the main class implements a logger this should always be true,
just in case a warning has been added to the README.md.
To implement a version callable by any thread look into [this](http://stackoverflow.com/questions/8275403/portable-way-to-find-name-of-main-class-from-java-code).
