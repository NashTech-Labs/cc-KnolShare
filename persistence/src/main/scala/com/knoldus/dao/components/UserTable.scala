package com.knoldus.dao.components

import com.knoldus.models.User

/**
 * Created by knoldus on 17/6/17.
 */
trait UserTable {
  this: DBComponent =>

  private[components] class UserTable extends Table[User](tag, "User") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val userName = column[String]("userName")
    val email = column[String]("email")
    val password = column[String]("password")

    def * = (id, userName, email, password) <> (User.tupled, User.unapply)
  }

  val userTableQuery = TableQuery[UserTable]
}
