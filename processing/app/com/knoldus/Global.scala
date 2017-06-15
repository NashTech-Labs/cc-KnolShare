import com.knoldus.streaming.kafka.{HashTagInitialiser, TweetConsumer}
import com.knoldus.streaming.twitter.TwitterFeed
import com.knoldus.utils.TwitterConfigReader
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings{

  override def onStart(app: Application): Unit = {
    new TwitterFeed().sendTweetsToKafka()
    HashTagInitialiser.hashTagCounter()

    val configReader = new TwitterConfigReader()
    val kafkaTopic = configReader.getKStreamTopic()
    val kafkaGroupId = "my-group"
    new TweetConsumer().consumeTweets(kafkaGroupId, kafkaTopic)
  }

}
