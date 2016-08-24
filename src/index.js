var scalajs = require('./logger-opt.js');
import BPromise from 'bluebird';
import _ from 'lodash';

const getFullErrorStack = ex => {
  var ret = ex.stack || ex.toString();
  if (ex.cause && typeof (ex.cause) === 'function') {
    const cex = ex.cause();
    if (cex) {
      ret += '\nCaused by: ' + getFullErrorStack(cex);
    }
  }
  return (ret);
};

const serializeError = potentialError => {
  if (potentialError instanceof Error) {
    if (!potentialError || !potentialError.stack) {
      return potentialError;
    }
    var obj = {
      message: potentialError.message,
      name: potentialError.name,
      stack: getFullErrorStack(potentialError),
      code: potentialError.code,
      signal: potentialError.signal
    };
    return obj;
  }
  return potentialError;
};

const methods = loggerRef => {
  const logMethods = {};
  ['debug', 'info', 'warn', 'error', 'fatal'].forEach(logLevel => {
    logMethods[logLevel] = (event, data, meta) => {
      loggerRef[logLevel](event, serializeError(data), meta);
    };
  });

  const logify = (func, event) => (...args) => {
    event = _.snakeCase(event).toUpperCase();
    let meta = args;
    if (args.length === 1 && _.isObject(args[0])) {
      meta = args[0];
    }
    return new BPromise((resolve, reject) => {
      loggerRef.debug(event, null, meta);
      func.apply(this, args)
        .then(result => {
          loggerRef.debug(`${event}_SUCCESS`, result, meta);
          resolve(result);
        })
        .catch(err => {
          loggerRef.error(`${event}_FAIL`, err, meta);
          reject(err);
        });
    });
  };

  const logifyAll = (obj, promisified = false) => {
    if (promisified === true) {
      promisified = 'Async';
    }
    for (const key in obj) {
      if (_.endsWith(key, promisified)) {
        obj[key] = logify(obj[key].bind(obj), _.trim(key, promisified));
      }
    }
    return obj;
  };

  return _.assign(logMethods, { logify, logifyAll });
};

export const create = info => {
  const loggerRef = scalajs.logger(info);

  return methods(loggerRef);
};

