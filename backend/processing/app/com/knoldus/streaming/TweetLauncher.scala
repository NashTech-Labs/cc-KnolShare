package com.knoldus.streaming

import play.api.libs.concurrent.Execution.Implicits._
import com.google.inject.Inject
import com.knoldus.streaming.kafka.{HashTagInitialiser, TweetConsumer}
import com.knoldus.streaming.twitter.TwitterFeed
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import play.api.{Environment, Mode}

class TweetLauncher @Inject()(environment: Environment, twitterFeed: TwitterFeed, tweetConsumer: TweetConsumer,
                              configReader: TwitterConfigReader) extends LoggerHelper {

  if (environment.mode != Mode.Test) {
    val kafkaTopic: String = configReader.getKStreamTopic()
    val kafkaGroupId: String = "my-group"
    getLogger(this.getClass).info("\n\n :::::::::::: Global is Running :::::::::::::")
    twitterFeed.sendTweetsToKafka()
    HashTagInitialiser.hashTagCounter()
    tweetConsumer.consumeTweets(kafkaGroupId, kafkaTopic)
  }
}