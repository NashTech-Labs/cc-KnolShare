package api

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import org.mockito.Mockito._
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import slack.api.SlackApiClient
import slack.models.Channel


class SlackApiTest extends WordSpecLike with MockitoSugar {

  val mockedApiClient: SlackApiClient = mock[SlackApiClient]

  object SlackApiTestObj extends SlackApi {
    val apiClient: SlackApiClient = mockedApiClient
  }

  "Slack Api " should {
    val one: Long = 1.toLong
    val seconds = 5
    "not send Slack Message when no channel found" in {
      when(SlackApiTestObj.apiClient.listChannels()).thenReturn(Future.successful(Seq()))
      val res: Boolean = Await
        .result(SlackApiTestObj.send("notify-me", "hellooo !!!", Some("user")),
          Duration(seconds, "seconds"))
      assert(res === false)
    }
    "send Slack Message" in {
      val time = 5
      when(SlackApiTestObj.apiClient.listChannels())
        .thenReturn(Future
          .successful(Seq(Channel("1",
            "notify-me",
            one,
            "me",
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None))))
      when(SlackApiTestObj.apiClient.postChatMessage("notify-me", "sending message on slack !! :)"))
        .thenReturn(Future("true"))
      val res: Boolean = Await
        .result(SlackApiTestObj.send("notify-me", "sending message on slack !! :)", Some("user")),
          Duration(time, "seconds"))
      assert(res === true)
    }

    "not send Slack Message due to incorrect channel name" in {
      val sec = 20
      when(SlackApiTestObj.apiClient.listChannels())
        .thenReturn(Future
          .successful(Seq(Channel("1",
            "notify-me",
            one,
            "abc",
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None,
            None))))
      when(SlackApiTestObj.apiClient.postChatMessage("channel", "wrong channel name"))
        .thenReturn(Future("false"))
      val res: Boolean = Await
        .result(SlackApiTestObj.send("channel", "sending message on slack !! :)", Some("user")),
          Duration(sec, "seconds"))
      assert(res === false)
    }
  }
}
