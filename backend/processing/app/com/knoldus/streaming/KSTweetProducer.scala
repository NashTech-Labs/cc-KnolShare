package com.knoldus.streaming

import java.util.Properties

import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.kafka.clients.producer.KafkaProducer


class KSTweetProducer @Inject()(@Named("KafkaProducerProps")
                                kafkaProducerProperties: Properties) {

  val kafkaProducer = new KafkaProducer[String, String](kafkaProducerProperties)

  def getKafkaProducer: KafkaProducer[String, String]= {
    kafkaProducer
  }

}
