package com.knoldus.dao.components

import scala.concurrent.Future

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.User
import scala.concurrent.ExecutionContext.Implicits.global

trait UserComponent extends UserTable {
  this: DBComponent =>

  import driver.api._

  def createUser(user: User): Future[User] = {
    db.run { userTableQuery returning userTableQuery.map(_.id) += user }
      .map(userId => user.copy(id = userId))
  }

  def updateUser(user: User): Future[Int] = {
    db.run(userTableQuery.filter(_.id === user.id).update(user))
  }

  def getUserById(userId: Int): Future[Option[User]] = {
    db.run(userTableQuery.filter(_.id === userId).result.headOption)
  }

  def deleteUserById(userId: Int) = {
    db.run(userTableQuery.filter(_.id === userId).delete)
  }

}

object UserComponent extends UserComponent with PostgresDBComponent
