package com.knoldus.guiceModules

import java.util.Properties

import com.google.inject.{Inject, Provider}
import com.knoldus.utils.TwitterConfigReader

class KProducerConfigProvider @Inject()(configReader: TwitterConfigReader) extends Provider[Properties] {
  override def get(): Properties = {
    val kafkaServers = configReader.getKafkaServers
    val config = new Properties()
    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    config.put("buffer.memory", "33554432")
    config.put("bootstrap.servers", kafkaServers)
    config.put("retries", "0")
    config.put("batch.size", "16384")
    config.put("linger.ms", "1")
    config.put("acks", "all")
    config
  }
}
