package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.UserSession
import slick.lifted.ProvenShape

trait UserSessionTable {

  this: DBComponent =>

  import driver.api._

  private[components] class UserSessionTable(tag: Tag) extends Table[UserSession](tag, "usersessions") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val email = column[String]("email")
    val accessToken = column[String]("accesstoken")

    def * : ProvenShape[UserSession] = (id.?, email, accessToken) <> ((UserSession.apply _).tupled, UserSession.unapply)
  }

  lazy val userSessionTableQuery = TableQuery[UserSessionTable]
}
