import com.knoldus.streaming.kafka.{HashTagInitialiser, TweetConsumer}
import com.knoldus.streaming.twitter.TwitterFeed
import com.knoldus.utils.TwitterConfigReader
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings{

  override def onStart(app: Application): Unit = {
    println("\n\n\n >>>>>>>>>>>>>>> Processing Global has started running >>>>>>>>>>>>>>> \n\n\n")
    new TwitterFeed().sendTweetsToKafka()
    HashTagInitialiser.hashTagCounter()

    val configReader = new TwitterConfigReader()
    val kafkaTopic = configReader.getKStreamTopic()
    val kafkaGroupId = "my-group"
    println("Kafka Topics: " + kafkaTopic)
    println("Kafka Group ID: " + kafkaGroupId)
    new TweetConsumer().consumeTweets(kafkaGroupId, kafkaTopic)
  }

}
