package services

import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.exceptions.PSqlException.{DatabaseException, InsertionError}
import com.knoldus.models.UserSession
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import service.UserSessionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserSessionServiceSpec extends PlaySpecification with Mockito {

  val mockUserSessionDbService  = mock[UserSessionDBService]

  object TestObject extends UserSessionService(mockUserSessionDbService)

  sequential
  "UserSessionService" should {

    val userSession = UserSession(None, "test@test.com", "accessToken")
    "be able to create userSession" in {
      mockUserSessionDbService.createUserSession(userSession) returns Future.successful(userSession.copy(id = Some(1)))
      val res = await(TestObject.createUserSession(userSession))
      res.email must beEqualTo("test@test.com")
    }

    "be able to create userSession: Failure case" in {
      mockUserSessionDbService.createUserSession(userSession) returns Future.failed(new Exception("Session cannot be created"))
      await(TestObject.createUserSession(userSession)) must throwA[Exception]
    }

    "be able to get User session" in {
      mockUserSessionDbService.getUserSessionByEmail("test@test.com") returns Future.successful(Some(userSession.copy(id = Some(1))))
      val res = await(TestObject.getUserSesionByEmail("test@test.com"))
      res.get.email must beEqualTo("test@test.com")
    }

    "be able to delete user session" in {
      mockUserSessionDbService.deleteUserSessionByEmail("test@test.com") returns Future.successful(1)
      val res = await(TestObject.deleteUserSessionByEmail("test@test.com"))
      res must beEqualTo(1)

    }

    "be able to delete user session: Failure case" in {
      mockUserSessionDbService.deleteUserSessionByEmail("test@test.com") returns Future.failed(DatabaseException("Session could not be deleted"))
      await(TestObject.deleteUserSessionByEmail("test@test.com")) must throwA(DatabaseException("Session could not be deleted"))
    }
  }

}
