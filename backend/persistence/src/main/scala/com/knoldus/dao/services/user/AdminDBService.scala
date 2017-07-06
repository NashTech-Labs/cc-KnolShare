package com.knoldus.dao.services.user

import scala.concurrent.Future

import com.google.inject.ImplementedBy
import com.knoldus.dao.components.AdminComponent
import com.knoldus.models.Admin

@ImplementedBy(classOf[AdminDBServiceImpl])
trait AdminDBService {

  val adminComponent: AdminComponent

  def getAdminByEmail(email: String): Future[Option[Admin]] = {
    adminComponent.getAdminByEmail(email)
  }
}

class AdminDBServiceImpl extends AdminDBService {
  val adminComponent = AdminComponent
}
