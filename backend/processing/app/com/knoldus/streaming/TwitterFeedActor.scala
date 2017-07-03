package com.knoldus.streaming

import akka.actor.Actor
import com.google.inject.Inject
import com.knoldus.models.Message
import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.utils.Constants._
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

class TwitterFeedActor @Inject()(configReader: TwitterConfigReader, tweetProducer: TweetProducer)
  extends Actor with LoggerHelper {

  override def receive: Receive = {
    case Message(PRODUCE_DATA) =>
      this.sendTweetsToKafka
  }

  private def sendTweetsToKafka(): Boolean = {

    val listener = new StatusListener() {
      def onStatus(status: Status): Unit = {
        val tweet = status.getText
        tweetProducer.send(tweet)
        getLogger(this.getClass).info("SENT :" + tweet)
      }

      def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) = {}

      def onTrackLimitationNotice(numberOfLimitedStatuses: Int) = {}

      def onScrubGeo(l: Long, l1: Long) = {}

      def onStallWarning(stallWarning: StallWarning) = {}

      def onException(ex: Exception): Unit = {
        ex.printStackTrace
      }
    }
    val twitterStream = getTwitterConfig
    twitterStream.addListener(listener)
    twitterStream.sample("en")
    true
  }

  private def getTwitterConfig: TwitterStream = {
    val twitterConf = new ConfigurationBuilder()
      .setOAuthConsumerKey(configReader.getTwitterConsumerKey)
      .setOAuthConsumerSecret(configReader.getTwitterConsumerSecretKey)
      .setOAuthAccessToken(configReader.getTwitterAccessToken)
      .setOAuthAccessTokenSecret(configReader.getTwitterAccessSecretToken).build
    new TwitterStreamFactory(twitterConf).getInstance
  }
}
