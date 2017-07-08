package service

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Random, Success, Try}
import com.google.inject.Inject
import com.knoldus.dao.services.user.{UserDBService, UserDBServiceImpl}
import com.knoldus.exceptions.NotificationException.MailerDaemonException
import com.knoldus.exceptions.PSqlException.{InsertionError, UserNotFoundException}
import com.knoldus.models.User
import com.knoldus.utils.Constants
import services.MailServiceImpl

import ExecutionContext.Implicits.global

class UserService @Inject()(userDBService: UserDBService, mailService: MailServiceImpl) {


  def validatePassWord(password: String, confirmPassword: String): Boolean = {
    password == confirmPassword
  }

  def createUser(user: User): Future[User] = {
    Try(userDBService.createUser(user)) match {
      case Success(user) => user
      case Failure(_) => throw InsertionError("unable to create the new user")
    }
  }

  def sendMail(listRecipents: List[String], subject: String, content: String): Future[Boolean] = {
    if (mailService.sendMail(listRecipents, subject, content)){
      Future(true)
    } else {
      Future.failed(MailerDaemonException("Failed to send the mail"))
    }
  }

  def validateUser(email: String): Future[User] = {
    Try(userDBService.getUserByEmail(email)) match {
      case Success(futureUserOpt) => futureUserOpt.map { userOpt =>
            userOpt.fold {
              throw UserNotFoundException("User With This Email Does Not Exists")
            } (identity)
        }

      case Failure(_) => throw UserNotFoundException("User With This Email Does Not Exists")
    }
  }
}
