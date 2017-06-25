package com.knoldus.streaming.kafka

import java.util.Properties

import com.google.inject.Inject
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.{Failure, Success, Try}

class TweetProducer @Inject()(configReader: TwitterConfigReader) extends LoggerHelper{

  def send(tweet: String): Boolean = {
    Try {
      val kafkaServers = configReader.getKafkaServers
      val properties = new Properties()
      properties.put("bootstrap.servers", kafkaServers)
      properties.put("acks", "all")
      properties.put("retries", "0")
      properties.put("batch.size", "16384")
      properties.put("linger.ms", "1")
      properties.put("buffer.memory", "33554432")
      properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      val kafkaProducer = new KafkaProducer[String, String](properties)
      getLogger(this.getClass).debug("\n\n Started Kafka Producer")
      val kafkaTopic = configReader.getKafkaTopic
      (kafkaTopic, kafkaProducer)
    } match {
      case Success(data) =>
        val (kafkaTopic, producer) = data
        producer.send(new ProducerRecord[String, String](kafkaTopic, tweet.toString))
        true
      case Failure(exception) => {
        getLogger(this.getClass).error(exception.getMessage)
        false
      }
    }
  }
}
