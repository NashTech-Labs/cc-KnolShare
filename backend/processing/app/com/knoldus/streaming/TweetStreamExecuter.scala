package com.knoldus.streaming

import java.util.Properties

import com.knoldus.utils.LoggerHelper
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.processor.TopologyBuilder
import org.apache.kafka.streams.state.Stores

class TweetStreamExecuter extends LoggerHelper {

  def execute(): Unit = {
    getLogger(this.getClass).info("The tweet execution has been  started")
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "trending-technology-on-twitter")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")
    props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
    props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)

    val builder = new TopologyBuilder
    builder.addSource("Source", "topic1")
    builder.addProcessor("Process", new TweetStreamProcessorSupplier, "Source")
    builder.addStateStore(Stores.create("stateStore").withStringKeys().withStringValues()
      .persistent().build(), "Process")
    builder.addSink("Sink", "topic2", "Process")

    val streams: KafkaStreams = new KafkaStreams(builder, props)
    streams.start()
  }
}
