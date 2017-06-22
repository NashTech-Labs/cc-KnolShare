package services

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import api.SlackApi
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}


class SlackServiceTest extends WordSpecLike with MustMatchers with MockitoSugar {

  private val mockedSlackApi = mock[SlackApi]
  private val seconds = 20

  object MockSlackServiceTestObject extends SlackService {
    val slackApi: SlackApi = mockedSlackApi
  }

  "Slack Service " should {

    "send notification on slack" in {
      when(mockedSlackApi.send("notify-me", "hey there !!", Some("user")))
        .thenReturn(Future.successful(true))
      assert(Await
        .result(MockSlackServiceTestObject.sendSlackMsg("notify-me", "hey there !!", Some("user")),
          Duration(seconds, "seconds")))
    }

  }
}