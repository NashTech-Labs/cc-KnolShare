package com.knoldus.streaming.kafka

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.utils.TwitterConfigReader
import com.typesafe.config.ConfigFactory
import org.scalatest.{AsyncWordSpecLike, BeforeAndAfterAll, MustMatchers}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class TweetConsumerActorSpec extends TestKit(ActorSystem("TestActorSystem",
  ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]"""))) with AsyncWordSpecLike
  with MustMatchers with MockitoSugar with BeforeAndAfterAll {

  private val mockedConfigReader = mock[TwitterConfigReader]

  val twitterConsumerProp: Props = Props(classOf[TweetConsumerActor], mockedConfigReader)
  private val tweetConsumerActor = TestActorRef[TweetConsumerActor](twitterConsumerProp)

  when(mockedConfigReader.getKafkaServers()) thenReturn "kafkaUrlPort"

}
