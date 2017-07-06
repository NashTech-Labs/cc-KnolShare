package api

import java.util.Properties
import javax.mail._
import javax.mail.internet._

import scala.util.{Failure, Success, Try}

import com.knoldus.utils.LoggerHelper
import utils.NotificationConfigReader

trait MailApi extends LoggerHelper {

  private val logger = getLogger(this.getClass)
  private val userEmail = NotificationConfigReader.email
  private val passWord = NotificationConfigReader.password
  private val port = "587"
  private val hostName = "smtp.gmail.com"

  private val properties = new Properties
  properties.put("mail.smtp.host", hostName)
  properties.put("mail.smtp.port", port)
  properties.put("mail.smtp.auth", "true")
  properties.put("mail.smtp.starttls.enable", "true")

  private val session = Session.getDefaultInstance(properties)


  def sendMail(recipients: List[String], subject: String, content: String): Boolean = {
    val message = new MimeMessage(session)
    Try(message.setFrom(new InternetAddress(userEmail))) match {
      case Success(_) => logger.info(s"Sending The Email from Email id $userEmail")
      case Failure(messagingException) => logger
        .error(s"Exception occurs ${ messagingException.getMessage }")

    }
    val recipientAddress: Array[Address] = (recipients map
                                            { recipient => new InternetAddress(recipient) }).toArray

    val transport = session.getTransport("smtp")
    Try {
      message.addRecipients(Message.RecipientType.TO, recipientAddress)
      message.setSubject(subject)
      message.setHeader("Content-Type", "text/plain;")
      message.setContent(content, "text/html")
      transport.connect(hostName, userEmail, passWord)
      transport.sendMessage(message, message.getAllRecipients)
    } match {
      case Success(_) => logger.info("Email Sent!!")
        true
      case Failure(messagingException) => logger.error(s"Messaging Exception $messagingException")
        false
    }
  }

}

object MailApiImpl extends MailApi
