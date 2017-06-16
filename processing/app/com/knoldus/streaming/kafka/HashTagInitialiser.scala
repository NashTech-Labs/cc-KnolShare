package com.knoldus.streaming.kafka

import java.util.Properties

import com.knoldus.utils.TwitterConfigReader
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object HashTagInitialiser {
  private val configReader = new TwitterConfigReader

  def hashTagCounter() {
    val streamsConfiguration = new Properties
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "find-top-hashtag")
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    val topic = configReader.getKStreamTopic
    val producerTopic = configReader.getKafkaTopic
    val stringSerde = Serdes.String
    val builder = new KStreamBuilder
    val textLines: KStream[String, String] = builder.stream(stringSerde, stringSerde, producerTopic)
    println(">>>>>>>>>>>>>>>>>>>>>>>>>> " + textLines + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    textLines.to(stringSerde, stringSerde, topic)
    val streams = new KafkaStreams(builder, streamsConfiguration)
    streams.start
    streams.close()
  }
}

