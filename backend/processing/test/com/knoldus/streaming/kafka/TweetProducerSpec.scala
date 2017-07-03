package com.knoldus.streaming.kafka

import com.knoldus.utils.TwitterConfigReader
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

class TweetProducerSpec extends PlaySpecification with Mockito{

  val mockedConfigReader = mock[TwitterConfigReader]
  val tweetProducer = new TweetProducer(mockedConfigReader)

  sequential
  "TweetProducerSpec" should {
    "tweet producer must be able to send tweets : Success Case" in {
      mockedConfigReader.getKafkaServers() returns "localhost:9092"
      mockedConfigReader.getKafkaTopic() returns "my-tweet"
      val response = tweetProducer.send("this is first tweet")
      response must beEqualTo(true)
    }

    "tweet producer must be able to send tweets : Failure Case" in  {
      mockedConfigReader.getKafkaServers() returns "server1"
      mockedConfigReader.getKafkaTopic() throws (new IllegalArgumentException("Unable to get Kafka server"))
      val response = tweetProducer.send("this is first tweet")
      response must beEqualTo(false)
    }
  }

}
