package com.knoldus.streaming.kafka


import com.google.inject.Inject
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import org.apache.kafka.clients.producer.ProducerRecord

import scala.util.{Failure, Success, Try}

class TweetProducer @Inject()(configReader: TwitterConfigReader, producer: KSKafkaProducer) extends LoggerHelper {

  def send(tweet: String): Boolean = {

    println(s"\n\nThe incoming tweet is : ${tweet.toString}")

    Try {
      val kafkaProducer = producer.getProducer
      println("\n\n Producing tweet to Kafka.....")
      val kafkaTopic = configReader.getKafkaTopic
      (kafkaTopic, kafkaProducer)
    } match {
      case Success(data) =>
        val (kafkaTopic, kProducer) = data
        kProducer.send(new ProducerRecord[String, String](kafkaTopic, tweet.toString))
        true
      case Failure(exception) => {
        getLogger(this.getClass).error(exception.getMessage)
        false
      }
    }
  }
}
