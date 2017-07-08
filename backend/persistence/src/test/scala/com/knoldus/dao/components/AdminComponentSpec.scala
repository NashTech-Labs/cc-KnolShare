package com.knoldus.dao.components

import com.knoldus.dao.connection.TestDBHelper
import com.knoldus.models.Admin
import play.api.test.PlaySpecification


class AdminComponentSpec extends PlaySpecification with AdminComponent with TestDBHelper {
  sequential
  "Admin Component" should {

    "be able to get admin by email" in {
      val email: Option[Admin] = await(getAdminByEmail("shivangi@gmail.com"))
      email must be equalTo Some(Admin(1,"shivangi@gmail.com", "1234"))
    }

  }
}
