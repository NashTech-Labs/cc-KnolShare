package com.knoldus.dao.services

import com.knoldus.dao.components.UserSessionComponent
import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.models.UserSession
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

import scala.concurrent.Future

class UserSessionDBServiceSpec extends PlaySpecification with Mockito {
  val mockedUserSessionComponent: UserSessionComponent = mock[UserSessionComponent]
  val userSession = UserSession(None, "test@test.com", "accessToken")

  object TestKnolxSessionDBService extends UserSessionDBService {
    val userSessionComponent: UserSessionComponent = mockedUserSessionComponent
  }

  sequential
  "UserSessionDBService" should {

    "be able to get userSession by email" in {
      mockedUserSessionComponent.getUserSessionByEmail("test@test.com") returns Future.successful(Some(userSession))
      val res = await(TestKnolxSessionDBService.getUserSessionByEmail("test@test.com"))
      res must beEqualTo(Some(userSession))
    }
  }

  "be able to create usersession" in {
    mockedUserSessionComponent.createUserSession(userSession) returns Future.successful(userSession.copy(id = Some(1)))
    val res = await(TestKnolxSessionDBService.createUserSession(userSession))
    res must beEqualTo(userSession.copy(id = Some(1)))
  }

  "be able to delete userSession" in {
    mockedUserSessionComponent.deleteUserSessionByEmail(userSession.email) returns Future.successful(1)
    val res = await(TestKnolxSessionDBService.deleteUserSessionByEmail(userSession.email))
    res must beEqualTo(1)
  }
}
