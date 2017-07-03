package services

import com.knoldus.dao.services.user.KnolxSessionDBService
import com.knoldus.models.KnolxSession
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import service.KnolxSessionService


class KnolxSessionServiceSpec extends PlaySpecification with Mockito {

  val mockedKnolxSessionDBService = mock[KnolxSessionDBService]

  object TestObject extends KnolxSessionService(mockedKnolxSessionDBService)

  sequential
  "KnolxSessionServiceSpec" should {
    "createKnolxSession" in {
      mockedKnolxSessionDBService.createKnolxSession(KnolxSession(1,"Shivangi",Some("abc"),Some(2),None,java.sql.Date.valueOf("2017-09-12")))

    }

  }
}
