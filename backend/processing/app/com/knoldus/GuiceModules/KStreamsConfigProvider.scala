package com.knoldus.GuiceModules

import java.util.Properties

import com.google.inject.Provider
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig


class KStreamsConfigProvider extends Provider[Properties] {
  override def get(): Properties = {
    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "find-top-hashtag")
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config
  }
}
