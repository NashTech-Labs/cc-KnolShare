package service

import com.google.inject.Inject
import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.exceptions.PSqlException.{DatabaseException, InsertionError}
import com.knoldus.models.UserSession

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class UserSessionService @Inject()(val userSessionDbService: UserSessionDBService) {

  def getUserSesionByEmail(email: String): Future[Option[UserSession]] = {
    userSessionDbService.getUserSessionByEmail(email)
  }

  def createUserSession(userSession: UserSession): Future[UserSession] = {
    Try(userSessionDbService.createUserSession(userSession)) match {
      case Success(userSession) => userSession
      case Failure(_) => throw InsertionError("Session cannot be created")
    }
  }

  def deleteUserSessionByEmail(email: String): Future[Int] = {
    Try(userSessionDbService.deleteUserSessionByEmail(email)) match {
      case Success(res) => res
      case Failure(_) => throw DatabaseException("Session could not be deleted")
    }
  }

}
