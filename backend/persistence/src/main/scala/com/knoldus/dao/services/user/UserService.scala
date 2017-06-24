package com.knoldus.dao.services.user

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject.{ImplementedBy, Singleton}
import com.knoldus.dao.components.UserComponent
import com.knoldus.models.User


@ImplementedBy(classOf[UserServiceImpl])
trait UserService {

  val userComponent: UserComponent

  def getUserByEmail(email: String): Future[Option[User]] = {
    userComponent.getUserByEmail(email)
  }

  def isUserExists(email: String, password: String): Future[Boolean] = {
    userComponent.getUserByEmailAndPassword(email, password).map {
      (userOpt: Option[User]) => userOpt.isDefined
    }
  }

  def createUser(user: User): Future[User] = {
    userComponent.createUser(user)
  }

}

@Singleton
class UserServiceImpl extends UserService {
  val userComponent = UserComponent
}
