package controllers

import scala.concurrent.Future
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.models.{Admin, UserSession}
import com.knoldus.utils.JsonResponse
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}
import service.{AdminService, UserService, UserSessionService}
import userHelper.{Helper, PassWordUtility}

class AdminControllerSpec extends PlaySpecification with Mockito {

  val mockedAdminService = mock[AdminService]
  private val mockedUserSessionService = mock[UserSessionService]

  private val mockedHelper = mock[Helper]

  private val mockedJsonResponse = mock[JsonResponse]

  private val mockedUserService = mock[UserService]

  private val mockedPasswordUtility = mock[PassWordUtility]

  //TODO: Remove JsonResponse with a mocked object
  private val adminController = new AdminController(
    mockedAdminService, mockedPasswordUtility, mockedHelper, JsonResponse, mockedUserSessionService)
  private val validUserLoginJson ="""{"email":"admin@gmail.com",
      |"password":"admin"}""".stripMargin

  sequential
  "AdminControllerSpec" should {

    "validate login for admin with valid email and password" in new WithApplication {
      val admin = Admin(1, "admin@gmail.com", "admin")
      when(mockedUserSessionService.createUserSession(UserSession(None, "admin@gmail.com", "accessToken"))). thenReturn(Future.successful(UserSession(Some(1), "admin@gmail.com", "accessToken")))

      mockedAdminService.validateUser("admin@gmail.com") returns Future.successful(admin)
      mockedPasswordUtility.verifyPassword("admin","admin") returns(true)
      mockedHelper.generateAccessToken returns "accessToken"

      val result = call(adminController.validateLogin,
        FakeRequest(POST, "/knolshare/admin/login")
          .withJsonBody(Json.parse(validUserLoginJson)))
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsString(result) mustEqual
      """{"data":{"admin":{"email":"admin@gmail.com"},"accessToken":"accessToken"}}"""
        .stripMargin
    }

    "invalidate login for admin with invalid email and password" in new WithApplication {
      val admin = Admin(1, "admin@gmail.com", "admin")
      mockedAdminService.validateUser("admin@gmail.com") returns Future.successful(admin)
      when(mockedUserSessionService.createUserSession(UserSession(None, "admin@gmail.com", "accessToken"))). thenReturn(Future.successful(UserSession(Some(1), "admin@gmail.com", "accessToken")))

      mockedPasswordUtility.verifyPassword("admin","admin") returns(false)
      mockedHelper.generateAccessToken returns "accessToken"

      val result = call(adminController.validateLogin,
        FakeRequest(POST, "/knolshare/admin/login")
          .withJsonBody(Json.parse(validUserLoginJson)))
      status(result) must equalTo(NOT_FOUND)
      contentType(result) must beSome("application/json")
      contentAsString(result) mustEqual
      """{"error":{"message":"Invalid UserName or Password"}}""".stripMargin
    }

    "Recover case : validate login for admin with email and password" in new WithApplication {
      val admin = Admin(1, "admin@gmail.com", "admin")
      mockedAdminService.validateUser("admin@gmail.com") returns Future.failed(new UserNotFoundException("User With This Email Does Not Exists"))
      mockedPasswordUtility.verifyPassword("admin","admin") returns(true)
      mockedHelper.generateAccessToken returns "accessToken"

      val result = call(adminController.validateLogin,
        FakeRequest(POST, "/knolshare/admin/login")
          .withJsonBody(Json.parse(validUserLoginJson)))
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome("application/json")
      contentAsString(result) mustEqual
      """{"error":{"message":"User With This Email Does Not Exists"}}""".stripMargin
    }

  }
}
