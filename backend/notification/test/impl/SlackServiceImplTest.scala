package impl

import api.SlackApi
import org.scalatest.WordSpecLike
import services.SlackServiceImpl


class SlackServiceImplTest extends WordSpecLike {
  "SlackServiceImpl" should {
    "be able to give a SlackApi Object back" in {
      val actual = SlackServiceImpl.slackApi
      assert(actual.isInstanceOf[SlackApi])
    }
  }
}
