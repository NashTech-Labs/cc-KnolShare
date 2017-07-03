package com.knoldus.streaming.kafka

import java.util.Collections

import akka.actor.Actor
import com.google.inject.Inject
import com.knoldus.models.ConsumeTweetMessage
import com.knoldus.utils.Constants._
import com.knoldus.utils.{Constants, LoggerHelper}
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._

case class KafkaTopic(kafkaTopic: String)

case class KafkaConsumerWrapper(consumer: KafkaConsumer[String, String])

class TweetConsumerActor @Inject()(consumer: KSKafkaConsumer)
  extends Actor with LoggerHelper {

  val kafkaConsumer: KafkaConsumer[String, String] = consumer.getConsumer

  override def receive: Receive = {
    case ConsumeTweetMessage(CONSUME_DATA, kafkaTopic: String) =>
      getLogger(this.getClass).info("\n\nStarting to consume the tweets from kafka\n\n")
      self ! KafkaTopic(kafkaTopic)

    case consumerForTopic: KafkaTopic =>
      getLogger(this.getClass).info(s"\n\nConsuming the tweets from kafka topic with consumer :" +
        s"${kafkaConsumer} with topic  : ${consumerForTopic.kafkaTopic}\n\n")
      kafkaConsumer.subscribe(Collections.singletonList(consumerForTopic.kafkaTopic))
      self ! KafkaConsumerWrapper(kafkaConsumer)

    case kafkaConsumerWrapper: KafkaConsumerWrapper =>
      val records = kafkaConsumerWrapper.consumer.poll(Constants.HUNDRED)
      getLogger(this.getClass).info("\n\nGoing to print the Records from Kafka\n\n")
      records.foreach { record =>
        getLogger(this.getClass).info("Received: " + record.key + " ---> " + record.value)
      }
      //TODO - Write logic to pass these tweets to ES
      self ! kafkaConsumerWrapper
  }
}
