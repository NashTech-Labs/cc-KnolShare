import com.knoldus.streaming.kafka.{HashTagInitialiser, TweetConsumer}
import com.knoldus.streaming.twitter.TwitterFeed
import com.knoldus.utils.{LoggerHelper, TwitterConfigReader}
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings with LoggerHelper {

  override def onStart(app: Application): Unit = {
    getLogger(this.getClass).info("\n\n ::::::::::::::::::::::::: Global is Running :::::::::::::::::::::::::")
    new TwitterFeed().sendTweetsToKafka()
    HashTagInitialiser.hashTagCounter()

    val configReader = new TwitterConfigReader()
    val kafkaTopic = configReader.getKStreamTopic()
    val kafkaGroupId = "my-group"
    new TweetConsumer().consumeTweets(kafkaGroupId, kafkaTopic)
  }

}
