package com.knoldus.dao.services.impl

import com.knoldus.dao.components.UserComponent
import com.knoldus.dao.services.user.UserDBServiceImpl
import play.api.test.PlaySpecification

class UserDBServiceImplTest extends PlaySpecification {

  "UserDBServiceImpl" should {

    "be able to return object of UserComponent" in {

      val userComponent = new UserDBServiceImpl().userComponent
      userComponent must beAnInstanceOf[UserComponent]
    }
  }
}
