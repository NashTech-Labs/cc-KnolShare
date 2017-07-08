package com.knoldus.dao.components

import com.knoldus.dao.connection.TestDBHelper
import com.knoldus.models.UserSession
import play.api.test.PlaySpecification

class UserSessionComponentSpec extends PlaySpecification with UserSessionComponent with TestDBHelper {

  sequential
  "UserSession Component" should {

    "be able to get userSession by email" in {
      val userSessionOpt = await(getUserSessionByEmail("test@test.com"))
      userSessionOpt must be equalTo Some(UserSession(Some(1), "test@test.com", "accessToken"))
    }

    "be able to create userSession at login and registration" in {
      val res = await(createUserSession(UserSession(None, "user@gmail.com", "1234")))
      res.email must beEqualTo("user@gmail.com")
      res.accessToken must beEqualTo("1234")
    }

    "be able to delete userSession on logout" in {
      val res  = await(deleteUserSessionByEmail("test@test.com"))
      res must beEqualTo(1)
    }
  }
}
