package com.knoldus.exceptions

object NotificationException {

  case class MailerDaemonException(message: String) extends Exception

}
