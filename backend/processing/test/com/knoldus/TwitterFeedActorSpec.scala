//package com.knoldus
//
//import akka.actor.ActorSystem
//import akka.testkit.TestKit
//import com.knoldus.streaming.kafka.TweetProducer
//import com.knoldus.streaming.twitter.TwitterFeedActor
//import com.knoldus.utils.TwitterConfigReader
//import org.specs2.mock.Mockito
//import play.api.test.{PlaySpecification, WithApplication}
//import twitter4j.TwitterStream
//
//class TwitterFeedActorSpec extends TestKit(ActorSystem()) with PlaySpecification with Mockito {
//
//  val mockedConfigReader = mock[TwitterConfigReader]
//  val mockedTweetProducer = mock[TweetProducer]
//  val twitterFeedActor = TestActorRef(new TwitterFeedActor(mockedConfigReader, mockedTweetProducer))
//
//  sequential
//  "TwitterFeedActorSpec" should {
//
//    "be able to get send tweets to kafka" in {
//      val status = twitterFeedActor.sendTweetsToKafka()
//      status must beEqualTo(true)
//    }
//
//    "be able to get TwitterStream Instance" in {
//      mockedConfigReader.getTwitterConsumerKey() returns "key"
//      mockedConfigReader.getTwitterConsumerSecretKey() returns "secret-key"
//      mockedConfigReader.getTwitterAccessToken() returns "access-token"
//      mockedConfigReader.getTwitterAccessSecretToken() returns "secret-access-token"
//
//      val configReader = twitterFeedActor.getTwitterConfig
//      configReader must beAnInstanceOf[TwitterStream]
//    }
//
//
//  }
//
//}
