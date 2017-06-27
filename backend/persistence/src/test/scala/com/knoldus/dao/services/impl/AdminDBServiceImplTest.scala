package com.knoldus.dao.services.impl

import com.knoldus.dao.components.AdminComponent
import com.knoldus.dao.services.user.AdminDBServiceImpl
import play.api.test.PlaySpecification

class AdminDBServiceImplTest extends PlaySpecification {
  "AdminDBServiceImpl" should {

    "be able to return object of AdminComponent" in {

      val adminComponent = new AdminDBServiceImpl().adminComponent
      adminComponent must beAnInstanceOf[AdminComponent]
    }
  }
}
