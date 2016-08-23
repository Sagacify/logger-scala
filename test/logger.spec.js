'use strict';

/* global describe, it */

var logger = require('../dist/index');
var assert = require('chai').assert;

describe('Logger', function () {
  it('The method "create" exists', function () {
    assert.isFunction(logger.create);
  });

  it('The method "create" takes one argument', function () {
    assert.equal(logger.create.length, 1);
  });

  var testLogger = logger.create({ module: 'test' });

  it('Created logger is an object', function () {
    assert.isObject(testLogger);
  });

  it('Created logger has 7 methods', function () {
    assert.equal(Object.keys(testLogger).length, 7);
  });

  it('Created logger "debug" method takes 3 arguments', function () {
    assert.equal(testLogger.debug.length, 3);
  });

  it('Created logger "info" method takes 3 arguments', function () {
    assert.equal(testLogger.info.length, 3);
  });

  it('Created logger "warn" method takes 3 arguments', function () {
    assert.equal(testLogger.warn.length, 3);
  });

  it('Created logger "error" method takes 3 arguments', function () {
    assert.equal(testLogger.error.length, 3);
  });

  it('Created logger "fatal" method takes 3 arguments', function () {
    assert.equal(testLogger.fatal.length, 3);
  });

  it('Created logger "logify" method takes 2 arguments', function () {
    assert.equal(testLogger.logify.length, 2);
  });

  it('Created logger "logifyAll" method takes 1 arguments', function () {
    assert.equal(testLogger.logifyAll.length, 1);
  });

  it('Created logger should log', function () {
    testLogger.fatal(
      'test',
      new Error('This is an error'),
      {'just to see': 'test'});
  });
});
