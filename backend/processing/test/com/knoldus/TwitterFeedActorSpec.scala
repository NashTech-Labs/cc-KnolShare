package com.knoldus

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.streaming.kafka.TweetProducer
import com.knoldus.streaming.twitter.TwitterFeedActor
import com.knoldus.utils.Constants.PRODUCE_DATA
import com.knoldus.utils.TwitterConfigReader
import org.scalatest.WordSpecLike
import org.specs2.mock.Mockito
import akka.util.Timeout
import akka.pattern.ask

import akka.util.JavaDurationConverters
import com.knoldus.models.Message

import scala.concurrent.Await
import scala.concurrent.duration._

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
      implicit val timeout = Timeout(10 seconds)
      val x: Boolean = Await.result(twitterFeedActor ? Message(PRODUCE_DATA), timeout.duration).asInstanceOf[Boolean]

      assert(x)
      expectNoMsg()
    }
  }

}