package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.User

trait UserTable {
  this: DBComponent =>

  import driver.api._

  private[components] class UserTable(tag: Tag) extends Table[User](tag, "User") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val userName = column[String]("userName")
    val email = column[String]("email")
    val password = column[String]("password")

    def * = (id, userName, email, password) <> (User.tupled, User.unapply)
  }

  val userTableQuery = TableQuery[UserTable]
}
