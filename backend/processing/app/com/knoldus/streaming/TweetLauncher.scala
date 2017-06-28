package com.knoldus.streaming

import akka.actor.{ActorSystem, Props}
import com.google.inject.Inject
import com.knoldus.models.{ConsumeTweetMessage, Message}
import com.knoldus.streaming.kafka.{StreamProcessorActor, TweetConsumerActor, TweetProducer}
import com.knoldus.streaming.twitter.TwitterFeedActor
import com.knoldus.utils.Constants._
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import play.api.{Environment, Mode}
import play.api.libs.concurrent.Execution.Implicits._

class TweetLauncher @Inject()(environment: Environment, configReader: TwitterConfigReader, tweetProducer: TweetProducer) extends LoggerHelper {

  val kafkaTopic: String = configReader.getKStreamTopic()
  val kafkaGroupId: String = "my-group"
  val processingGlobalActor = ActorSystem("ProcessingGlobalActor")
  val tweetProducerActor = processingGlobalActor.actorOf(Props(classOf[TwitterFeedActor], configReader, tweetProducer), "TweetProducer")
  val kafkaStreamProcessor = processingGlobalActor.actorOf(Props(classOf[StreamProcessorActor], configReader), "KafkaStreamProcessorActor")
  val tweetConsumerActor = processingGlobalActor.actorOf(Props(classOf[TweetConsumerActor], configReader), "TweetConsumerActor")

  // $COVERAGE-OFF$
  if (environment.mode != Mode.Test) {
    tweetProducerActor ! Message(PRODUCE_DATA)
    kafkaStreamProcessor ! Message(PROCESS_DATA)
    tweetConsumerActor ! ConsumeTweetMessage(CONSUME_DATA, kafkaGroupId, kafkaTopic)
  }
  // $COVERAGE-ON$

  processingGlobalActor.terminate()
}
