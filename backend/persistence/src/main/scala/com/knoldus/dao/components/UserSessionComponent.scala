package com.knoldus.dao.components

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.UserSession
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

trait UserSessionComponent extends UserSessionTable {
  this: DBComponent =>

  import driver.api._

  def getUserSessionByEmail(email: String): Future[Option[UserSession]] = {
    db.run(userSessionTableQuery.filter(_.email === email).result.headOption)
  }

  def createUserSession(userSession: UserSession): Future[UserSession] = {
    db.run {
      userSessionTableQuery returning userSessionTableQuery.map(_.id) += userSession
    }.map(userSessionId => userSession.copy(id = Some(userSessionId)))
  }

  def deleteUserSessionByEmail(email: String): Future[Int] = {
    db.run(userSessionTableQuery.filter(_.email === email).delete)
  }
}

object UserSessionComponent extends UserSessionComponent with PostgresDBComponent
