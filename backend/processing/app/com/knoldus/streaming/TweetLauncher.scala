package com.knoldus.streaming

import akka.actor.{ActorSystem, Props}
import com.google.inject.Inject
import com.knoldus.models.{ConsumeTweetMessage, Message}
import com.knoldus.streaming.kafka.{StreamProcessor, TweetConsumerActor}
import com.knoldus.streaming.twitter.TwitterFeedActor
import com.knoldus.utils.Constants._
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import play.api.{Environment, Mode}
import play.api.libs.concurrent.Execution.Implicits._

class TweetLauncher @Inject()(environment: Environment, configReader: TwitterConfigReader) extends LoggerHelper {
  getLogger(this.getClass).info("\n\n :::::::::::: Global is Running :::::::::::::")
  val kafkaTopic: String = configReader.getKStreamTopic()
  val kafkaGroupId: String = "my-group"
  val processingGlobalActor = ActorSystem("ProcessingGlobalActor")
  val tweetProducerActor = processingGlobalActor.actorOf(Props[TwitterFeedActor], "Monitor")
  val kafkaStreamProcessor = processingGlobalActor.actorOf(Props[StreamProcessor], "Attendent")
  val tweetConsumerActor = processingGlobalActor.actorOf(Props[TweetConsumerActor])

  if (environment.mode != Mode.Test) {
    getLogger(this.getClass).info("\n\n :::::::::::: STARTED PRODUCING :::::::::::::")

    tweetProducerActor ! Message(PRODUCE_DATA)
    getLogger(this.getClass).info("\n\n :::::::::::: STARTED PROCESSING :::::::::::::")

    kafkaStreamProcessor ! Message(PROCESS_DATA)
    getLogger(this.getClass).info("\n\n :::::::::::: STARTED CONSUMING :::::::::::::")

    tweetConsumerActor ! ConsumeTweetMessage(CONSUME_DATA, kafkaGroupId, kafkaTopic)
  }
  processingGlobalActor.terminate()
}

