package com.knoldus.streaming.kafka

import java.util.Properties

import com.knoldus.utils.TwitterConfigReader
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class TweetProducer {
  private val configReader = new TwitterConfigReader

  def send(tweet: String): Unit = {
    val kafkaServers = configReader.getKafkaServers
    val kafkaTopic = configReader.getKafkaTopic
    val properties = new Properties()
    properties.put("bootstrap.servers", kafkaServers)
    properties.put("acks", "all")
    properties.put("retries", "0")
    properties.put("batch.size", "16384")
    properties.put("linger.ms", "1")
    properties.put("buffer.memory", "33554432")
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    try {
      val producer = new KafkaProducer[String, String](properties)
      producer.send(new ProducerRecord[String, String](kafkaTopic, tweet.toString))
    } catch {
      case ex: Exception => {
        ex.printStackTrace()
      }
    }
  }
}
