package services

import scala.concurrent.Future

import com.knoldus.dao.services.user.AdminDBService
import com.knoldus.models.Admin
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import service.AdminService

class AdminServiceSpec extends PlaySpecification with Mockito {

  val mockedAdminDBService = mock[AdminDBService]
  object TestObject extends AdminService(mockedAdminDBService)

  "AdminService should" {

    "validate admin for valid email and password" in {
      val admin = Admin(1, "admin@gmail.com", "password")
      mockedAdminDBService.getAdminByEmail("admin@gmail.com") returns Future.successful(Some(admin))
      val result = await(TestObject.validateUser("admin@gmail.com"))
      result must beEqualTo(admin)
    }
  }

}
