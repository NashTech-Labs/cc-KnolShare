package com.knoldus.exceptions


object PSqlException {

  case class InsertionError(message: String) extends Exception

  case class UserNotFoundException(message: String) extends Exception

  case class DatabaseException(message: String) extends Exception

}
