package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.User
import slick.lifted.ProvenShape

trait UserTable {
  this: DBComponent =>

  import driver.api._

  private[components] class UserTable(tag: Tag) extends Table[User](tag, "users") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val userName = column[String]("username")
    val email = column[String]("email")
    val password = column[String]("password")
    val phoneNumber = column[String]("phone_num")

    def * : ProvenShape[User] = (id, userName, email, password, phoneNumber) <> ((User.apply _).tupled, User.unapply)
  }

  lazy val userTableQuery = TableQuery[UserTable]
}
