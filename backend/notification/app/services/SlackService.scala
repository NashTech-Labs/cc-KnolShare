package services

import scala.concurrent.Future

import api.{SlackApi, SlackApiImpl}


trait SlackService {

  val slackApi: SlackApi

  /**
   *
   * this method calls the send(..) method of the SlackApi
   */
  def sendSlackMsg(channelName: String, msgBody: String, user: Option[String]): Future[Boolean] = {
    slackApi.send(channelName, msgBody, user)
  }
}

object SlackServiceImpl extends SlackService {
  val slackApi: SlackApi = SlackApiImpl
}
