package com.knoldus.streaming.kafka

import java.util.Properties
import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.kafka.clients.consumer.KafkaConsumer

class KSKafkaConsumer @Inject()(@Named("KafkaConsumerProps") properties: Properties) {
  def getConsumer: KafkaConsumer[String, String] = {
    new KafkaConsumer(properties)
  }
}
