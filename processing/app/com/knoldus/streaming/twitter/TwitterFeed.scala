package com.knoldus.streaming.twitter

import java.io.IOException

import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.utils.TwitterConfigReader
import twitter4j.conf.ConfigurationBuilder
import twitter4j._

class TwitterFeed {
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
  def sendTweetsToKafka() {
    val listener = new StatusListener() {
      def onStatus(status: Status) {
        val tweet = status.getText
        new TweetProducer().send(tweet)
        System.out.println("Sent: " + tweet)
      }

      def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {
        //Do Nothing
      }

      def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {
        //Do Nothing
      }

      def onScrubGeo(l: Long, l1: Long) {
        //Do Nothing
      }

      def onStallWarning(stallWarning: StallWarning) {
        //Do Nothing
      }

      def onException(ex: Exception) {
        ex.printStackTrace
      }
    }
    val twitterStream = getTwitterConfig
    twitterStream.addListener(listener)
    twitterStream.sample("en")
  }

}
