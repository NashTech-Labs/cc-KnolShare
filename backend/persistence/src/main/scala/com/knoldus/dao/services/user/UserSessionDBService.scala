package com.knoldus.dao.services.user

import com.google.inject.{ImplementedBy, Singleton}
import com.knoldus.dao.components.UserSessionComponent
import com.knoldus.models.UserSession

import scala.concurrent.Future

@ImplementedBy(classOf[UserSessionDBServiceImpl])
trait UserSessionDBService {

  val userSessionComponent : UserSessionComponent

  def getUserSessionByEmail(email: String): Future[Option[UserSession]] = {
    userSessionComponent.getUserSessionByEmail(email)
  }

  def createUserSession(userSession: UserSession): Future[UserSession] = {
    userSessionComponent.createUserSession(userSession)
  }

  def deleteUserSessionByEmail(email: String): Future[Int] = {
    userSessionComponent.deleteUserSessionByEmail(email)
  }
}

object UserSessionDBService extends UserSessionDBServiceImpl

@Singleton
class UserSessionDBServiceImpl extends UserSessionDBService {
   val userSessionComponent = UserSessionComponent
}
