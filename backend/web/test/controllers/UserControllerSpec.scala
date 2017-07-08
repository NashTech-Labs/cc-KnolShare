package controllers

import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.exceptions.NotificationException.MailerDaemonException
import com.knoldus.exceptions.PSqlException.{DatabaseException, InsertionError, UserNotFoundException}
import com.knoldus.models.{User, UserSession}
import com.knoldus.utils.{Constants, JsonResponse}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.libs.json.{JsValue, Json}
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}
import service.{UserService, UserSessionService}
import userHelper.{Helper, PassWordUtility}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserControllerSpec extends PlaySpecification with MockitoSugar {

  private val mockedHelper = mock[Helper]
  private val mockedUserService = mock[UserService]
  private val mockedPasswordUtility = mock[PassWordUtility]
  private val mockedUserSessionService = mock[UserSessionService]
  private val mockUserSessionDbService = mock[UserSessionDBService]

  private val userRequestJson =
    """{"userName":"anubhav","email":"anubhavtarar40@gmail.com",
      |"password":"anubhav","confirmPassword":"anubhav","phoneNumber":"8588915184"}""".stripMargin
  private val json = Json.parse(userRequestJson)

  private val userRequestJsonWithDiffPassWord =
    """{"userName":"anubhav","email":"anubhavtarar40@gmail.com",
      |"password":"xyzr","confirmPassword":"anubhav","phoneNumber":"8588915184"}""".stripMargin

  private val invalidUserLoginJson =
    """{"email":"wrongId@gmail.com",
      |"password":"yz"}""".stripMargin

  private val validUserLoginJson =
    """{"email":"anubhavtarar40@gmail.com",
      |"password":"anubhav"}""".stripMargin

  private val invalidUserRequestJson =
    """{"userName":"","email":"anubhavtarar40@gmail.com",
      |"password":"anubhav","confirmPassword":"anubhav","phoneNumber":"8588915184"}""".stripMargin

  private val emptyJsonForRegistration =
    """{"userName":"","email":"",
      |"password":"","confirmPassword":"","phoneNumber":""}""".stripMargin

  private val emptyJsonForLogin = """{"email":"", "password":""}""".stripMargin

  //TODO: Remove JsonResponse with a mocked object
  private val userController = new UserController(mockedUserService, mockedPasswordUtility, mockedHelper, JsonResponse, mockedUserSessionService, mockUserSessionDbService)

  private val user = User(None, "anubhav", "anubhavtarar40@gmail.com", "anubhav", "8588915184")

  "new user must get created with valid user request json" in new WithApplication {
    val res: JsValue = Json.parse(
      """{"data":{"userName":"anubhav","email":"anubhavtarar40@gmail.com",
        |"phoneNumber":"8588915184"},"accessToken":"accessToken"}""".stripMargin)
    when(mockedPasswordUtility.hashedPassword("anubhav")).thenReturn("anubhav")
    when(mockedHelper.generateAccessToken).thenReturn("accessToken")
    //when(mockedJsonResponse.successResponse(userResponse.toJson, Some(JsString("accessToken")))).thenReturn(res.as[JsObject])
    when(mockedUserService.createUser(user)).thenReturn(Future.successful(user))

    when(mockedUserService.validatePassWord("anubhav", "anubhav")).thenReturn(true)
when(mockedUserSessionService.createUserSession(UserSession(None, "anubhavtarar40@gmail.com", "accessToken"))). thenReturn(Future.successful(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken")))

    when(mockedUserService.sendMail(List("anubhavtarar40@gmail.com"), Constants.MAIL_SUBJECT,
      Constants.MAIL_BODY)).thenReturn(Future(true))

    val result = call(userController.registerUser, FakeRequest(POST, "/knolshare/register").withJsonBody(json))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("\"email\":\"anubhavtarar40@gmail.com\"")
  }

  "new user must not get created with invalid user request json" in new WithApplication {

    val result = call(userController.registerUser, FakeRequest(POST, "/knolshare/register")
      .withJsonBody(Json.parse(invalidUserRequestJson)))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual """{"error":{"message":"wrong json content "}}"""

  }

  "new user must not get created with empty json in user request" in new WithApplication {
    when(mockedUserService.validatePassWord("", "")).thenReturn(false)

    val result = call(userController.registerUser,
      FakeRequest(POST, "/knolshare/register")
        .withJsonBody(Json.parse(emptyJsonForRegistration)))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"wrong json content "}}"""

  }

  "new user must not get created when password and confirm password is not same in user request json" in
    new WithApplication {

      when(mockedUserService.validatePassWord("xyzr", "anubhav")).thenReturn(false)

      val result = call(userController.registerUser,
        FakeRequest(POST, "/knolshare/register")
          .withJsonBody(Json.parse(userRequestJsonWithDiffPassWord)))

      status(result) must equalTo(NOT_FOUND)
      contentType(result) must beSome("application/json")
      contentAsString(result) mustEqual
        """{"error":{"message":"password and confirm password do not match "}}"""
    }

  "new user must not get created with invalid user request json" in new WithApplication {
    val result = call(userController.registerUser,
      FakeRequest(POST, "/knolshare/register")
        .withJsonBody(Json.parse(invalidUserRequestJson)))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"wrong json content "}}"""

  }

  "new user must not get created if postgres connection failed" in new WithApplication {
    when(mockedPasswordUtility.hashedPassword("anubhav")).thenReturn("anubhav")
    when(mockedUserService.createUser(user)).thenReturn(Future.failed(
      InsertionError("unable to create the new user")))
    when(mockedUserService.validatePassWord("anubhav", "anubhav")).thenReturn(true)

    val result = call(userController.registerUser, FakeRequest(POST, "/knolshare/register").withJsonBody(json))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"unable to create the new user"}}"""
  }

  "new user must creation scenario for mailing failure" in new WithApplication {
    val responseJson =
      """{
        |"data":
        |{
        |"user":
        |{
        |"userName":"anubhav",
        |"email":"anubhavtarar40@gmail.com",
        |"phoneNumber":"8588915184"
        |},
        |"accessToken":"accessToken"
        |}
        |}""".stripMargin

    when(mockedPasswordUtility.hashedPassword("anubhav")).thenReturn("anubhav")
    when(mockedUserService.createUser(user)).thenReturn(Future.successful(user))
    when(mockedUserService.sendMail(List("anubhavtarar40@gmail.com"), Constants.MAIL_SUBJECT, Constants.MAIL_BODY))
      .thenReturn(Future.failed(MailerDaemonException("hghdjs")))
    when(mockedUserService.validatePassWord("anubhav", "anubhav")).thenReturn(true)
    when(mockedHelper.generateAccessToken).thenReturn("accessToken")
    when(mockedUserSessionService.createUserSession(UserSession(None, "anubhavtarar40@gmail.com", "accessToken"))). thenReturn(Future.successful(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken")))


    val result = call(userController.registerUser, FakeRequest(POST, "/knolshare/register").withJsonBody(json))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("accessToken\":\"accessToken")
  }

  "user must not be able to login with invalid email id" in new WithApplication {
    when(mockedUserService.validateUser("wrongId@gmail.com"))
      .thenReturn(Future.failed(UserNotFoundException("User With This Email Does Not Exists")))

    val result = call(userController.validateLogin,
      FakeRequest(POST, "/knolshare/login")
        .withJsonBody(Json.parse(invalidUserLoginJson)))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"User With This Email Does Not Exists"}}"""
  }

  "user must not be able to login with valid email id and invalid password" in new WithApplication {
    when(mockedUserService.validateUser("anubhavtarar40@gmail.com")).thenReturn(Future.successful(user))
    when(mockedPasswordUtility.verifyPassword("anubhav", "anubhav")).thenReturn(false)

    val result = call(userController.validateLogin,
      FakeRequest(POST, "/knolshare/login")
        .withJsonBody(Json.parse(validUserLoginJson)))

    status(result) must equalTo(NOT_FOUND)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"Invalid UserName or Password"}}"""
  }

  "user must not be able to login with empty json" in new WithApplication {
    when(mockedUserService.validateUser(""))
      .thenReturn(Future.failed(UserNotFoundException("User With This Email Does Not Exists")))

    val result = call(userController.validateLogin,
      FakeRequest(POST, "/knolshare/login")
        .withJsonBody(Json.parse(emptyJsonForLogin)))

    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"User With This Email Does Not Exists"}}"""

  }

  "user must be able to login with valid email id and valid password" in new WithApplication {
    when(mockedUserService.validateUser("anubhavtarar40@gmail.com")).thenReturn(Future.successful(user))
    when(mockedPasswordUtility.verifyPassword("anubhav", "anubhav")).thenReturn(true)
    when(mockedHelper.generateAccessToken).thenReturn("accessToken")
    when(mockedUserSessionService.createUserSession(UserSession(None, "anubhavtarar40@gmail.com", "accessToken"))). thenReturn(Future.successful(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken")))


    val result = call(userController.validateLogin,
      FakeRequest(POST, "/knolshare/login")
        .withJsonBody(Json.parse(validUserLoginJson)))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"data":{"user":{"userName":"anubhav","email":"anubhavtarar40@gmail.com","phoneNumber":"8588915184"},"accessToken":"accessToken"}}""".stripMargin
  }

  "user must be able to logout" in new WithApplication {
    when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))
    when(mockedUserSessionService.deleteUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(1))
    val result = call(userController.logout, FakeRequest(GET, "/knolshare/logout")
      .withSession("accessToken" -> "accessToken").withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com"))
    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"data":{"message":"User Logged Out successfully !!"}}""".stripMargin
  }

  "unsuccessfull logout when user not found" in new WithApplication {

    when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(None))

    val result = call(userController.logout, FakeRequest(GET, "/knolshare/logout")
      .withSession("accessToken" -> "accessToken").withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com"))
    status(result) must equalTo(UNAUTHORIZED)
    contentType(result) must beSome("text/plain")
    contentAsString(result) mustEqual "User Does not Exist !!"
  }

  "unsuccessfull logout when accessToken is incorrect" in new WithApplication {

    when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))

    val result = call(userController.logout, FakeRequest(GET, "/knolshare/logout")
      .withSession("accessToken" -> "accessToken").withHeaders("accessToken" -> "access", "email" -> "anubhavtarar40@gmail.com"))
    status(result) must equalTo(UNAUTHORIZED)
    contentType(result) must beSome("text/plain")
    contentAsString(result) mustEqual "Unauthorized Access !!"
  }

  "user logout: Recover case due to dbException" in new WithApplication {
    when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))
    when(mockedUserSessionService.deleteUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.failed( new DatabaseException("Unsuccessful LOGOUT")))
    val result = call(userController.logout, FakeRequest(GET, "/knolshare/logout")
      .withSession("accessToken" -> "accessToken").withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com"))
    status(result) must equalTo(BAD_REQUEST)
    contentType(result) must beSome("application/json")
    contentAsString(result) mustEqual
      """{"error":{"message":"Unsuccessful LOGOUT"}}""".stripMargin
  }

}
