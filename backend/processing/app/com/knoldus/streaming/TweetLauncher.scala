package com.knoldus.streaming

import akka.actor.{ActorSystem, Props}
import com.google.inject.Inject
import com.knoldus.models.{ConsumeTweetMessage, Message}
import com.knoldus.streaming.kafka._
import com.knoldus.utils.Constants._
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import play.api.{Environment, Mode}

class TweetLauncher @Inject()(environment: Environment, configReader: TwitterConfigReader,
                              tweetProducer: TweetProducer, kafkaConsumer: KSKafkaConsumer) extends LoggerHelper {

  val kafkaTopic: String = configReader.getKStreamTopic()
  val kafkaGroupId: String = configReader.getGroupId()
  val processingGlobalActor = ActorSystem("ProcessingGlobalActor")
  val tweetProducerActor = processingGlobalActor.actorOf(Props(classOf[TwitterFeedActor], configReader, tweetProducer), "TweetProducer")
  val kafkaStreamProcessor = processingGlobalActor.actorOf(Props(classOf[StreamProcessorActor], configReader), "KafkaStreamProcessorActor")
  val tweetConsumerActor = processingGlobalActor.actorOf(Props(classOf[TweetConsumerActor], kafkaConsumer), "TweetConsumerActor")

  if (environment.mode != Mode.Test) {
    getLogger(this.getClass).info("\n\n ::::::::::::::::::::::::: Activating Tweet Producer :::::::::::::::::::::: \n\n")
    tweetProducerActor ! Message(PRODUCE_DATA)
    getLogger(this.getClass).info("\n\n ::::::::::::::::::::::::: Activating Producer Actor :::::::::::::::::::::: \n\n")
    kafkaStreamProcessor ! Message(PROCESS_DATA)
    getLogger(this.getClass).info("\n\n ::::::::::::::::::::::::: Activating Stream Processor :::::::::::::::::::::: \n\n")
    tweetConsumerActor ! ConsumeTweetMessage(CONSUME_DATA, kafkaTopic)
  }

  //processingGlobalActor.terminate()
}
