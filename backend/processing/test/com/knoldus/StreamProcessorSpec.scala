package com.knoldus

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.streaming.kafka.{StreamProcessor, TweetProducer}
import com.knoldus.utils.Constants.PROCESS_DATA
import com.knoldus.utils.TwitterConfigReader
import org.scalatest.WordSpecLike
import org.specs2.mock.Mockito

class StreamProcessorSpec extends TestKit(ActorSystem()) with WordSpecLike with Mockito {

  val mockedConfigReader = mock[TwitterConfigReader]
  val mockedTweetProducer = mock[TweetProducer]

  val processingReq = Props(classOf[StreamProcessor], mockedConfigReader)
  val streamProcessorActor = TestActorRef[StreamProcessor](processingReq)

  "com.knoldus.TwitterFeedActorSpec" should {

    "com.knoldus.TwitterFeedActorSpec" in {
      mockedConfigReader.getKafkaServers() returns "localhost:9092"
      mockedConfigReader.getKafkaTopic() returns "my-tweet"
      streamProcessorActor ! PROCESS_DATA
      expectNoMsg()
    }
  }

}