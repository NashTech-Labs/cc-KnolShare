package impl

import api.SlackApiImpl
import org.scalatest.WordSpecLike
import slack.api.SlackApiClient

class SlackApiImplTest extends WordSpecLike {
  "SlackApiImpl" should {
    "be able to give a SlackApi Object back" in {
      val actual = SlackApiImpl.apiClient
      assert(actual.isInstanceOf[SlackApiClient])
    }
  }
}
