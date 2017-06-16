package com.knoldus.streaming.kafka

import java.util.{Collections, Properties}

import com.knoldus.utils.TwitterConfigReader
import org.apache.kafka.clients.consumer.KafkaConsumer

class TweetConsumer {
  private val configReader = new TwitterConfigReader

  def consumeTweets(groupId: String, kafkaTopic: String) {
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
      val records = kafkaConsumer.poll(100)
      import scala.collection.JavaConversions._
      for (record <- records) {
        System.out.println("Received: " + record.key + " ---> " + record.value)
      }
      System.out.println("\n\n=======================\n\n")
    }
  }


}
