package service

import com.google.inject.Inject
import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.models.UserSession

import scala.concurrent.Future

class UserSessionService @Inject()(val userSessionDbService: UserSessionDBService) {

  def getUserSesionByEmail(email: String): Future[Option[UserSession]] = {
    userSessionDbService.getUserSessionByEmail(email)
  }

  def createUserSession(userSession: UserSession): Future[UserSession] = {
    userSessionDbService.createUserSession(userSession)
  }

  def deleteUserSessionByEmail(email: String): Future[Int] = {
    userSessionDbService.deleteUserSessionByEmail(email)
  }

}
