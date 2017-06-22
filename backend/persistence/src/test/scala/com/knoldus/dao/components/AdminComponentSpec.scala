package com.knoldus.dao.components

import com.knoldus.dao.connection.TestDBHelper
import play.api.test.PlaySpecification


class AdminComponentSpec extends PlaySpecification with AdminComponent with TestDBHelper {
  sequential
  "Admin Component" should {

    "be able to get admin by email" in {
      val email = await(getAdminByEmail("shivangi@gmail.com"))
      email.size must be equalTo 1
    }

  }
}
