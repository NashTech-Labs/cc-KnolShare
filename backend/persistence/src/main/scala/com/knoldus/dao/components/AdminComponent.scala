package com.knoldus.dao.components

import scala.concurrent.Future

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.Admin

trait AdminComponent extends AdminTable{
  this: DBComponent =>

  import driver.api._

  def getAdminByEmail(emailId: String): Future[Option[Admin]] = {
    db.run(adminTableQuery.filter(_.email === emailId).result.headOption)
  }

}

object AdminComponent extends AdminComponent with PostgresDBComponent

