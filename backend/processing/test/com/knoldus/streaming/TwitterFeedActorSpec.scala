package com.knoldus.streaming

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{EventFilter, TestActorRef, TestKit}
import akka.util.Timeout
import com.knoldus.models.Message
import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.utils.Constants.PRODUCE_DATA
import com.knoldus.utils.TwitterConfigReader
import com.typesafe.config.ConfigFactory
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Await
import scala.concurrent.duration._

class TwitterFeedActorSpec extends TestKit(ActorSystem("TestActorSystem",
  ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]"""))) with WordSpecLike
  with MustMatchers with MockitoSugar with BeforeAndAfterAll{

  val mockedConfigReader = mock[TwitterConfigReader]
  val mockedTweetProducer = mock[TweetProducer]

  val twitterFeedReq = Props(classOf[TwitterFeedActor], mockedConfigReader, mockedTweetProducer)
  val twitterFeedActor = TestActorRef[TwitterFeedActor](twitterFeedReq)

  when(mockedConfigReader.getTwitterConsumerKey()) thenReturn "key"
  when(mockedConfigReader.getTwitterConsumerSecretKey()) thenReturn "secret-key"
  when(mockedConfigReader.getTwitterAccessToken()) thenReturn "access-token"
  when(mockedConfigReader.getTwitterAccessSecretToken()) thenReturn "access-secret-token"

  "TwitterFeedActor" should {
    "send tweets to kafka" in {
      EventFilter.info("Tweets sent to Kafka") intercept {
        twitterFeedActor ! Message(PRODUCE_DATA)
      }
    }
  }

}