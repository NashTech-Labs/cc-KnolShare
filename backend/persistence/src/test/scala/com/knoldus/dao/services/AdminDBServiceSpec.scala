package com.knoldus.dao.services

import scala.concurrent.Future

import com.knoldus.dao.components.AdminComponent
import com.knoldus.dao.services.user.AdminDBService
import com.knoldus.models.Admin
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

/**
 * Created by knoldus on 23/6/17.
 */
class AdminDBServiceSpec extends PlaySpecification with Mockito {

  val mockedAdminComponent = mock[AdminComponent]

  object TestService extends AdminDBService {
    val adminComponent = mockedAdminComponent
  }

  "Admin Service" should {
    "be able to get user by email" in {
      mockedAdminComponent.getAdminByEmail("admin@gmail.com") returns Future.successful(Some(Admin(1, "email", "password")))
      val res = await(TestService.getAdminByEmail("admin@gmail.com"))
      res.get.email must beEqualTo("email")
      res.get.password must beEqualTo("password")
    }
  }
}
