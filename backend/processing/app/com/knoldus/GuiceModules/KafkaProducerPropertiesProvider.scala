package com.knoldus.GuiceModules

import java.util.Properties

import com.google.inject.{Inject, Provider}
import com.knoldus.utils.TwitterConfigReader

class KafkaProducerPropertiesProvider @Inject()(configReader: TwitterConfigReader) extends Provider[Properties]{
  override def get(): Properties = {
    val kafkaServers = configReader.getKafkaServers
    val properties = new Properties()
      properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("buffer.memory", "33554432")
    properties.put("bootstrap.servers", kafkaServers)
    properties.put("retries", "0")
    properties.put("batch.size", "16384")
    properties.put("linger.ms", "1")
    properties.put("acks", "all")
    properties
  }
}
