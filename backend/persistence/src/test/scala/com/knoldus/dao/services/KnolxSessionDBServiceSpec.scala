package com.knoldus.dao.services

import com.knoldus.dao.components.KnolxSessionComponent
import com.knoldus.dao.services.user.KnolxSessionDBService
import com.knoldus.models.KnolxSession
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

import scala.concurrent.Future

class KnolxSessionDBServiceSpec extends PlaySpecification with Mockito {

  val mockedKnolxSessionComponent: KnolxSessionComponent = mock[KnolxSessionComponent]

  object TestKnolxSessionDBService extends KnolxSessionDBService {
    val knolxSessionComponent: KnolxSessionComponent = mockedKnolxSessionComponent
  }

  sequential
  "KnolxSession Service" should {

    "be able to create knolx session" in {
      mockedKnolxSessionComponent.createKnolxSession(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12"))) returns
        Future.successful(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12")))
      val res = await(TestKnolxSessionDBService.createKnolxSession(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12"))))
      res.id must beEqualTo(1)
    }

    "get knolx session by Presenter's name " in {
      mockedKnolxSessionComponent.getKnolxByPresentor("Shivangi") returns
        Future.successful(Some(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12"))))
      val res = await(TestKnolxSessionDBService.getKnolxByPresenter("Shivangi"))
      res.size must beEqualTo(1)
    }

    "get knolx session by date" in {
      mockedKnolxSessionComponent.getKnolxByDate(java.sql.Date.valueOf("2017-09-12")) returns
        Future.successful(Some(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12"))))
      val res = await(TestKnolxSessionDBService.getKnolxByDate(java.sql.Date.valueOf("2017-09-12")))
      res.size must beEqualTo(1)
    }

    "be able to get knolx session by date and presenter's name " in {
      mockedKnolxSessionComponent.getKnolxByNameAndDate("Shivangi", java.sql.Date.valueOf("2017-09-12")) returns
        Future.successful(Some(KnolxSession(1, "Shivangi", Some("abc"), None, None, java.sql.Date.valueOf("2017-09-12"))))
      val res = await(TestKnolxSessionDBService.getKnolxByNameAndDate("Shivangi", java.sql.Date.valueOf("2017-09-12")))
      res.size must beEqualTo(1)
    }

    "be able to update knolx by id " in {
      mockedKnolxSessionComponent.updateKnolxById(1, KnolxSession(1, "Shivangi", Some("abc"), Some(1), None, java.sql.Date.valueOf("2017-09-12"))) returns
        Future.successful(1)
      val res = await(
        future = TestKnolxSessionDBService.updateKnolxById(1, KnolxSession(1, "Shivangi", Some("abc"), Some(1), None, java.sql.Date.valueOf("2017-09-12"))))
      res must beEqualTo(1)
    }

    "be able to delete knolx by id " in {
      mockedKnolxSessionComponent.deleteKnolxById(1) returns
        Future.successful(1)
      val res = await(TestKnolxSessionDBService.deleteKnolxById(1))
      res must beEqualTo(1)
    }
  }

}