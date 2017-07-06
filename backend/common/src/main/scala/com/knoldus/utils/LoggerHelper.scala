package com.knoldus.utils

import org.slf4j.{Logger, LoggerFactory}

// $COVERAGE-OFF$Disabling highlighting by default until a workaround for https://issues.scala-lang.org/browse/SI-8596 is found
trait LoggerHelper {
  def getLogger(clazz: Class[_]): Logger = {
    LoggerFactory.getLogger(clazz)
  }
}

// $COVERAGE-ON$