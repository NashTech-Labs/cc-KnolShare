package controllers

import java.sql.Date

import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.models.{KnolxSession, UserSession}
import com.knoldus.utils.{Constants, JsonResponse}
import org.mockito.Mockito.when
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}
import service.KnolxSessionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KnolxSessionControllerSpec extends PlaySpecification with Mockito {

  val mockedKnolxSessionService = mock[KnolxSessionService]
  private val mockUserSessionDbService = mock[UserSessionDBService]


  private val knolxSessionController = new KnolxSessionController(mockedKnolxSessionService, mockUserSessionDbService)

  private val validUserLoginJson =
    """{"email":"admin@gmail.com",
      |"password":"admin"}""".stripMargin

  sequential
  "KnolxSessionControllerSpec" should {

    "getAllKnolxSession" in new WithApplication {
      val date = new Date(System.currentTimeMillis())
      when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))

      mockedKnolxSessionService.getAllKnolxSession() returns Future(List(KnolxSession(1, "presenter", None,
        None, None, date)))

      val result: Future[Result] = call(knolxSessionController.getAllKnolxSession, FakeRequest(GET,
        "/knolshare/knolxSession/getAll").withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com" ))

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsString(result) must contain("\"id\":1,\"presentor\":\"presenter\"")
    }

    "getAllKnolxSession : Failure" in new WithApplication {
      val date = new Date(System.currentTimeMillis())
      when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))

      mockedKnolxSessionService.getAllKnolxSession() returns Future.failed(new Exception("Database Exception"))

      val result: Future[Result] = call(knolxSessionController.getAllKnolxSession, FakeRequest(GET,
        "/knolshare/knolxSession/getAll").withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com" ))

      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome("application/json")
      contentAsString(result) must contain("{\"error\":{\"message\":\"Internal Server Error\"}}")
    }

    /*"Create Knolx Session : Unauthorised access" in new WithApplication() {
      when(mockUserSessionDbService.getUserSessionByEmail("anubhavtarar40@gmail.com")). thenReturn(Future.successful(Some(UserSession(Some(1), "anubhavtarar40@gmail.com", "accessToken"))))

      val date = new Date(1502496000000L)
      val knolxSessionRequestJson =
        s"""{"id":"-1",
           |"presenter":"sangeeta gulia",
           |"topic":"scala",
           |"sessionId":"1",
           |"rating":"4",
           |"scheduledDate":"${date.toString}"}""".stripMargin
      val knolxSession = KnolxSession(-1, "sangeeta gulia", Some("scala"), Some(Constants.ONE),
        Some(Constants.FOUR), date)
      mockedKnolxSessionService.createKnolxSession(knolxSession) returns {
        Future(knolxSession)
      }

      val request = FakeRequest(POST, "/knolshare/knolxSession/create").withJsonBody(Json.parse(knolxSessionRequestJson)).withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken", "email" -> "anubhavtarar40@gmail.com" )
      val result: Future[Result] = call(knolxSessionController.createKnolxSession, request)

      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome("application/json")
    }
*/
/*    "Create Knolx Session" in new WithApplication() {
      val date = new Date(System.currentTimeMillis())
      val knolxSessionRequestJson =
        s"""{"id":"1",
           |"presenter":"sangeeta gulia",
           |"topic":"scala",
           |"sessionId":"1",
           |"rating":"4",
           |"scheduledDate":"${date.toString}"}""".stripMargin
      val knolxSession = KnolxSession(-1, "sangeeta gulia", Some("scala"), Some(Constants.ONE),
        Some(Constants.FOUR), date)
      println("knolx session in test" + knolxSession)
      mockedKnolxSessionService.createKnolxSession(knolxSession) returns {
        println("returning mocked result")
        Future(knolxSession)
      }

      val request = FakeRequest(POST, "/knolshare/knolxSession/create").withJsonBody(Json.parse(knolxSessionRequestJson))
        .withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken")
      val result: Future[Result] = call(knolxSessionController.createKnolxSession, request)

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
    }*/

  }
}
