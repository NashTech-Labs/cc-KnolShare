package com.knoldus.streaming

import javax.inject.Inject

import com.google.inject.name.Named
import com.knoldus.utils.LoggerHelper
import org.apache.kafka.clients.producer.ProducerRecord
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, _}
import com.knoldus.utils.TechnologyName._

class TweetReceiver @Inject()(@Named("twitterStream") twitterStream: TwitterStream,
                              kSTweetProducer: KSTweetProducer, filterQuery: FilterQuery )
  extends StatusListener with LoggerHelper {

  override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {

  }

  override def onScrubGeo(l: Long, l1: Long): Unit = {

  }

  /**
    * Send the status to the Kafka so that others can access
    *
    * @param status The tweet with all information with it
    *               We need only the tweet text as of now so
    *               tweet text is sent to the Kafka Topic
    */
  override def onStatus(status: Status): Unit = {
    kSTweetProducer.getKafkaProducer.send(new ProducerRecord("topic1", status.getText))
  }

  override def onTrackLimitationNotice(i: Int): Unit = {

  }

  override def onStallWarning(stallWarning: StallWarning): Unit = {

  }

  override def onException(e: Exception): Unit = {

  }

  def startReceivingTweets(): Unit = {
    twitterStream.addListener(this)

    filterQuery.language("en")

    filterQuery.track("akka", "elasticsearch", "elastic search", "akkahttp", "neo4j", "postgres",
      "mysql", "playframework", "play framework", "kafka", "flink", "orango_db", "redis")

    twitterStream.filter(filterQuery)
  }
}
