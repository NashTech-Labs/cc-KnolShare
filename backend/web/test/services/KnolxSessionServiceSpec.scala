package services

import com.knoldus.dao.services.user.KnolxSessionDBService
import com.knoldus.models.KnolxSession
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import service.KnolxSessionService
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class KnolxSessionServiceSpec extends PlaySpecification with Mockito {

  val mockedKnolxSessionDBService = mock[KnolxSessionDBService]

  object TestObject extends KnolxSessionService(mockedKnolxSessionDBService)

  sequential
  "KnolxSessionServiceSpec" should {
    "be able to createKnolxSession" in {
      val knolSession: KnolxSession = KnolxSession(1,"Shivangi",Some("abc"),Some(2),None,java.sql.Date.valueOf("2017-09-12"))
      mockedKnolxSessionDBService.createKnolxSession(knolSession) returns Future.successful(knolSession)
      val result: KnolxSession = await(TestObject.createKnolxSession(knolSession))
      result.id must beEqualTo(1)
    }

    "be able to get all knolx sessions" in {
      val knolSession: KnolxSession = KnolxSession(1,"Shivangi",Some("abc"),Some(2),None,java.sql.Date.valueOf("2017-09-12"))
      mockedKnolxSessionDBService.getAllKnolxSessions() returns(Future(List(knolSession)))
      val result: List[KnolxSession] = await(TestObject.getAllKnolxSession())
      result.size must beEqualTo(1)
    }
  }
}
