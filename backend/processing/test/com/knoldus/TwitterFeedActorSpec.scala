package com.knoldus

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.streaming.twitter.TwitterFeedActor
import com.knoldus.utils.Constants.PRODUCE_DATA
import com.knoldus.utils.TwitterConfigReader
import org.scalatest.WordSpecLike
import org.specs2.mock.Mockito

class TwitterFeedActorSpec extends TestKit(ActorSystem()) with WordSpecLike with Mockito {

  val mockedConfigReader = mock[TwitterConfigReader]
  val mockedTweetProducer = mock[TweetProducer]

  val twitterFeedReq = Props(classOf[TwitterFeedActor], mockedConfigReader, mockedTweetProducer)
  val twitterFeedActor = TestActorRef[TwitterFeedActor](twitterFeedReq)

  "com.knoldus.TwitterFeedActorSpec" should {

    "com.knoldus.TwitterFeedActorSpec" in {
      mockedConfigReader.getTwitterConsumerKey() returns "key"
      mockedConfigReader.getTwitterConsumerSecretKey() returns "secret-key"
      mockedConfigReader.getTwitterAccessToken() returns "access-token"
      mockedConfigReader.getTwitterAccessSecretToken() returns "access-secret-token"
      twitterFeedActor ! PRODUCE_DATA
      expectNoMsg()
    }
  }

}