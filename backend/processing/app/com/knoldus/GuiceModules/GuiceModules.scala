package com.knoldus.GuiceModules

import java.util.Properties

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.knoldus.streaming.TweetLauncher
import com.knoldus.streaming.kafka.{KSKafkaConsumer, TweetConsumerActor}
import com.knoldus.utils.TwitterConfigReader

class GuiceModules extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[Properties])
      .annotatedWith(Names.named("KafkaConsumerProps"))
      .toProvider(classOf[KafkaConsumerPropertiesProvider])

    bind(classOf[Properties])
      .annotatedWith(Names.named("KafkaProducerProps"))
      .toProvider(classOf[KafkaProducerPropertiesProvider])

    bind(classOf[KSKafkaConsumer])
    bind(classOf[TwitterConfigReader])
    bind(classOf[TweetConsumerActor])

    bind(classOf[TweetLauncher]).asEagerSingleton()
  }
}
