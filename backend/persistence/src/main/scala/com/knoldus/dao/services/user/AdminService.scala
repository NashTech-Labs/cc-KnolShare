package com.knoldus.dao.services.user

import scala.concurrent.Future

import com.knoldus.dao.components.AdminComponent
import com.knoldus.models.Admin

trait AdminService {

  val adminComponent: AdminComponent

  def getAdminByEmail(email: String): Future[Option[Admin]] = {
    adminComponent.getAdminByEmail(email)
  }
}

class AdminServiceImpl extends AdminService {
  val adminComponent = AdminComponent
}
