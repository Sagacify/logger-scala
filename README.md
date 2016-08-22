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



# Sagacify Standarized Logger

## Environment Variables

```bash
LOGENTRIES_TOKEN # (Optional) Use it to put your logentries token
LOG_LEVEL # (Optional) Define you log level, defaut to 'info'
```

## Usage

```js
var log = require('saga-logger')
  .create({ module: 'files-controller' });

log.debug('event', {
  someData : data
}, {
  someMetadata : metadata
});
```

Data and metadata are optional.
Data can be an `Error` object

Here are the different logging methods:

```js
log.debug(event, data, metadata);
log.info(event, data, metadata);
log.warn(event, data, metadata);
log.error(event, data, metadata);
log.fatal(event, data, metadata);
```

It is also possible to logify a function returning a promise:
```js
const logifiedFunc = log.logify(func, event);
```
It will wrap the func(args) function in a promise that:
- log.debug(event, null, args)
- execute func
- if success, log.debug(event + '_SUCCESS', result, args), resolve with result
- if fail, log.error(event + '_FAIL', err, args), reject with err

To replace all functions returning promise of an object:
```js
log.logifyAll(obj, promisified);
```
Promisified is optional and is used to pass the suffix of methods generated with the Bluebird promisifyAll method. Here is an example of how to use it to promisify and then logify aws-sdk library:
```
import BPromise from 'bluebird';
import AWS from 'aws-sdk';
const log = require('./src').create({ module: 'sqs' });

AWS.config.update({
  accessKeyId: 'accessKeyId',
  secretAccessKey: 'secretAccessKey'
});
const sqs = new AWS.SQS();

// this will add promisified versions of sqs methods, with 'Async' suffix
const promisifiedSqs = BPromise.promisifyAll(sqs);

// this will replace all promisified versions of sqs methods by their logified versions
log.logifyAll(promisifiedSqs, true);
```
