package com.knoldus.streaming.kafka

import java.util.{Collections, Properties}

import com.knoldus.utils.{Constants, LoggerHelper, TwitterConfigReader}
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._

class TweetConsumer extends LoggerHelper {
  private val configReader = new TwitterConfigReader

  def consumeTweets(groupId: String, kafkaTopic: String): Unit = {
    val kafkaServers = configReader.getKafkaServers
    val props = new Properties
    props.put("bootstrap.servers", kafkaServers)
    props.put("group.id", groupId)
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("auto.offset.reset", "earliest")
    props.put("session.timeout.ms", "30000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer")
    val kafkaConsumer = new KafkaConsumer(props)
    kafkaConsumer.subscribe(Collections.singletonList(kafkaTopic))
    while (true) {
      val records = kafkaConsumer.poll(Constants.HUNDRED)
      for (record <- records) {
        getLogger(this.getClass).info("Received: " + record.key + " ---> " + record.value)
      }
      getLogger(this.getClass).info("\n\n=======================\n\n")
    }
  }


}
