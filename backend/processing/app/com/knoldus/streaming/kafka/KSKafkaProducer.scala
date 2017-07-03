package com.knoldus.streaming.kafka

import java.util.Properties

import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer

class KSKafkaProducer @Inject()(@Named("KafkaProducerProps") val properties: Properties) {
  def getProducer: KafkaProducer[String, String] = {
    new KafkaProducer(properties)
  }
}
