package controllers

import java.sql.Date
import java.text.SimpleDateFormat

import akka.stream.ActorMaterializer
import com.knoldus.exceptions.PSqlException.DatabaseException
import com.knoldus.models.KnolxSession
import com.knoldus.utils.{Constants, JsonResponse}
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}
import service.KnolxSessionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KnolxSessionControllerSpec extends PlaySpecification with Mockito {

  val mockedKnolxSessionService = mock[KnolxSessionService]
  private val mockedJsonResponse = mock[JsonResponse]

  private val knolxSessionController = new KnolxSessionController(mockedKnolxSessionService, JsonResponse)

  private val validUserLoginJson =
    """{"email":"admin@gmail.com",
      |"password":"admin"}""".stripMargin

  sequential
  "KnolxSessionControllerSpec" should {

    "getAllKnolxSession" in new WithApplication {
      val date=new Date(System.currentTimeMillis())
      mockedKnolxSessionService.getAllKnolxSession() returns Future(List(KnolxSession(1, "presenter", None,
        None, None, date)))

      val result: Future[Result] = call(knolxSessionController.getAllKnolxSession, FakeRequest(GET,
        "/knolshare/knolxSession/getAll").withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken"))

        status (result) must equalTo(OK)
        contentType (result) must beSome("application/json")
        contentAsString(result) must contain("\"id\":1,\"presentor\":\"presenter\"")
    }

    "getAllKnolxSession : Failure" in new WithApplication {
      val date=new Date(System.currentTimeMillis())
      mockedKnolxSessionService.getAllKnolxSession() returns Future.failed(new Exception("Database Exception"))

      val result: Future[Result] = call(knolxSessionController.getAllKnolxSession, FakeRequest(GET,
        "/knolshare/knolxSession/getAll").withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken"))

      status (result) must equalTo(BAD_REQUEST)
      contentType (result) must beSome("application/json")
      println(contentAsString(result))
      contentAsString(result) must contain("{\"error\":{\"message\":\"Internal Server Error\"}}")
    }

    "Create Knolx Session" in new WithApplication() {
      val date=new Date(System.currentTimeMillis())
      val knolxSessionRequestJson =
      s"""{"id":"1",
        |"presenter":"sangeeta gulia",
        |"topic":"scala",
        |"sessionId":"1",
        |"rating":"4",
        |"scheduledDate":"${date.toString}"}""".stripMargin
      val json = Json.parse(knolxSessionRequestJson)
      val knolxSession = KnolxSession(-1, "sangeeta gulia", Some("scala"), Some(Constants.ONE),
        Some(Constants.FOUR), date)
      println("knolx session in test" + knolxSession)
      mockedKnolxSessionService.createKnolxSession(knolxSession) returns Future(knolxSession.copy(id=1))

      val request = FakeRequest(POST,
        "/knolshare/knolxSession/create").withJsonBody(Json.parse(knolxSessionRequestJson))
        .withSession("accessToken" -> "accessToken")
        .withHeaders("accessToken" -> "accessToken")
      val result = knolxSessionController.createKnolxSession(request)

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      println(contentAsString(result))
      contentAsString(result) must contain("presenter")
    }
  }
}
