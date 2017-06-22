package com.knoldus.dao.components

import com.knoldus.dao.connection.TestDBHelper
import com.knoldus.models.KnolxSession
import play.api.test.PlaySpecification


class KnolxSessionComponentSpec extends PlaySpecification with KnolxSessionComponent with TestDBHelper {

  sequential
  "KnolxSessionComponent Component" should {

    "be able to create new knolx session" in {
      val knolx: KnolxSession = await(createKnolxSession(KnolxSession(2, "shivangi", Some("xyz"), Some(2), None, java.sql.Date.valueOf("2017-09-12"))))
      println(knolx)
      knolx.id must be equalTo 2
    }

    "be able to update knolx" in {
      val updateCount = await(updateKnolxById(1, KnolxSession(2, "shivangi", Some("xyz"), Some(2), Some(4), java.sql.Date.valueOf("2017-09-12"))))
      updateCount must beEqualTo(1)
    }

    "be able to delete a scheduled knolx session" in {
      val deleteCount = await(deleteKnolxById(1))
      deleteCount must beEqualTo(1)
    }

    "be able to get knolx by presentor" in {
      val knol: Option[KnolxSession] = await(getKnolxByPresentor("Geetika"))
      knol.get.id must beEqualTo(1)
    }

    "be able to get knolx by date" in {
      val knol: Option[KnolxSession] = await(getKnolxByDate(java.sql.Date.valueOf("2017-12-21")))
      knol.size must beEqualTo(1)
    }
  }


}
