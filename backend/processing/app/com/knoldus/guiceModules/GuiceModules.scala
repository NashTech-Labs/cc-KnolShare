package com.knoldus.guiceModules

import java.util.Properties

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.knoldus.streaming.ProcessingLauncher
import twitter4j.TwitterStream

class GuiceModules extends AbstractModule {
  override def configure(): Unit = {

    bind(classOf[Properties])
      .annotatedWith(Names.named("KafkaProducerProps"))
      .toProvider(classOf[KProducerConfigProvider])

    bind(classOf[Properties])
      .annotatedWith(Names.named("streamConfig"))
      .toProvider(classOf[KStreamsConfigProvider])

    bind(classOf[TwitterStream])
      .annotatedWith(Names.named("twitterStream"))
      .toProvider(classOf[TwitterStreamProvider])

    bind(classOf[ProcessingLauncher]).asEagerSingleton()
  }
}