package api

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

import com.typesafe.config.ConfigFactory
import slack.api.SlackApiClient

trait SlackApi {
  val conf = ConfigFactory.load()
  val apiClient: SlackApiClient
  val token = conf.getString("slack.token")

  /**
   *
   * this method sends a msgBody on the channel named channelName as user
   */
  def send(channelName: String, msgBody: String, user: Option[String]): Future[Boolean] = {
    val channelIdOpt = getChannelId(channelName)
    channelIdOpt.map {
      case Some(channelId) =>
        apiClient.postChatMessage(channelId, msgBody, user)
        true
      case None =>
        false

    }
  }

  /**
   *
   * this method returns the channel id of the channel named channelName
   */
  private def getChannelId(channelName: String): Future[Option[String]] = {
    val channelsFutureOpt = apiClient.listChannels().map(channels =>
      channels.map(channel =>
        if (channel.name == channelName) {
          Some(channel.id)
        }
        else {
          None
        }
      ))
    channelsFutureOpt.map(channels => channels.find(channel => channel.isDefined).flatten)
  }
}

object SlackApiImpl extends SlackApi {
  val apiClient: SlackApiClient = SlackApiClient(token)
}