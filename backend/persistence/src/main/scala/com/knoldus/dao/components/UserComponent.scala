package com.knoldus.dao.components

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.User

trait UserComponent extends UserTable {
  this: DBComponent =>

  import driver.api._

  def createUser(user: User): Future[User] = {
    db.run {
      userTableQuery returning userTableQuery.map(_.id) += user
    }
      .map(userId => user.copy(id = userId))
  }

  def updateUserById(id: Int, user: User): Future[Int] = {
    db.run {
      userTableQuery.filter(_.id === id).update(user)
    }
  }

  def getUserById(userId: Int): Future[Option[User]] = {
    db.run(userTableQuery.filter(_.id === userId).result.headOption)
  }

  def getUserByEmail(email: String): Future[Option[User]] = {
    db.run(userTableQuery.filter(_.email === email).result.headOption)
  }

  def deleteUserById(userId: Int): Future[Int] = {
    db.run(userTableQuery.filter(_.id === userId).delete)
  }

  def getUserByEmailAndPassword(email: String, password: String): Future[Option[User]] = {
    db.run {
      userTableQuery.filter { userTable =>
        userTable.email === email && userTable.password === password
      }.result.headOption
    }
  }
}

object UserComponent extends UserComponent with PostgresDBComponent