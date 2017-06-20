package com.knoldus.streaming.twitter

import java.io.IOException

import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import twitter4j.conf.ConfigurationBuilder
import twitter4j._

class TwitterFeed extends LoggerHelper {
  private val configReader = new TwitterConfigReader

  private def getTwitterConfig = {
    val twitterConf = new ConfigurationBuilder()
      .setOAuthConsumerKey(configReader.getTwitterConsumerKey)
      .setOAuthConsumerSecret(configReader.getTwitterConsumerSecretKey)
      .setOAuthAccessToken(configReader.getTwitterAccessToken)
      .setOAuthAccessTokenSecret(configReader.getTwitterAccessSecretToken).build
    new TwitterStreamFactory(twitterConf).getInstance
  }

  @throws[TwitterException]
  @throws[IOException]
  def sendTweetsToKafka(): Unit = {
    val listener = new StatusListener() {
      def onStatus(status: Status): Unit = {
        val tweet = status.getText
        new TweetProducer().send(tweet)
        getLogger(this.getClass).info("Sent: " + tweet)
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
  }

}
