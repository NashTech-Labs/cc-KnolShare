package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.Admin
import slick.lifted.ProvenShape


trait AdminTable {
  this: DBComponent =>

  import driver.api._

  private[components] class AdminTable(tag: Tag) extends Table[Admin](tag, "admin") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val email = column[String]("email")
    val password = column[String]("password")

    def * : ProvenShape[Admin] = (id, email, password) <> ((Admin.apply _).tupled, Admin.unapply)
  }

  lazy val adminTableQuery = TableQuery[AdminTable]
}
