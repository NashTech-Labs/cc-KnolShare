package com.knoldus.GuiceModules

import java.util.Properties

import com.google.inject.{Inject, Provider}
import com.knoldus.utils.TwitterConfigReader

class KafkaConsumerPropertiesProvider @Inject()(configReader: TwitterConfigReader)
  extends Provider[Properties]{

    override def get(): Properties = {
        val props = new Properties
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("enable.auto.commit", "true")
        props.put("bootstrap.servers", configReader.getKafkaServers())
        props.put("group.id", configReader.getGroupId())
        props.put("auto.commit.interval.ms", "1000")
        props.put("auto.offset.reset", "earliest")
        props.put("session.timeout.ms", "30000")
        props
    }

}
