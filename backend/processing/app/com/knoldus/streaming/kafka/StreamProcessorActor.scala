package com.knoldus.streaming.kafka

import java.util.Properties

import akka.actor.Actor
import com.google.inject.Inject
import com.knoldus.models.Message
import com.knoldus.utils.Constants._
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import scala.util.{Failure, Success, Try}

class StreamProcessorActor @Inject()(configReader: TwitterConfigReader) extends Actor with LoggerHelper {

  override def receive: Receive = {
    case Message(PROCESS_DATA) => hashTagCounter()
  }

  def hashTagCounter(): Boolean = {
    Try {
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
      getLogger(this.getClass).info(">>>>>>>>>>>>>>>> " + textLines + ">>>>>>>>>>>>>>>>")
      textLines.to(stringSerde, stringSerde, topic)
      val streams = new KafkaStreams(builder, streamsConfiguration)
      streams.start
      streams.close()
      true
    } match {
      case Success(data) => data
      case Failure(exception) => getLogger(this.getClass).debug(exception.getMessage)
        false
    }
  }

  override def postStop(): Unit = {
    getLogger(this.getClass).info("Stream processor actor has been stopped.")
  }
}





