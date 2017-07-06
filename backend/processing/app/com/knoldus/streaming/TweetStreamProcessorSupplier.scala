package com.knoldus.streaming

import com.google.inject.Inject
import com.knoldus.inMemoryDB.RedisHandler
import com.knoldus.utils.{Constants, LoggerHelper, TechnologyNames}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier}
import org.apache.kafka.streams.state.{KeyValueIterator, KeyValueStore}

import scala.collection.mutable


class TweeterKafkaProcessor @Inject()(technologyNames: TechnologyNames, redisHandler: RedisHandler)
  extends Processor[String, String] with LoggerHelper {

  var context: ProcessorContext = null
  var kvStore: KeyValueStore[String, String] = null
  val technologies: List[String] = technologyNames.get()
  val technologyCounterMap: mutable.Map[String, Long] = new mutable.HashMap[String, Long]()

  technologies.map{ technology =>
    technologyCounterMap += (technology -> 0L)
  }

  override def init(processorContext: ProcessorContext): Unit = {
    this.context = processorContext
    this.kvStore = context.getStateStore("stateStore").asInstanceOf[KeyValueStore[String, String]]
    this.context.schedule(Constants.THOUSAND)
    getLogger(this.getClass).info("\n:::: The streaming of tweets has been started! ::::")
  }

  override def process(key: String, tweet: String): Unit = {
    this.kvStore.put(tweet, tweet)
    this.context.commit()
  }

  override def punctuate(l: Long): Unit = {

    val tweetIterator: KeyValueIterator[String, String] = kvStore.all

    technologyCounterMap.drop(technologyCounterMap.size) //First clean the map
    while (tweetIterator.hasNext) {
      val tweet = tweetIterator.next()
      parseTweetAndStoreStats(tweet.value)
    }
    redisHandler.ingestStatuses(technologyCounterMap.toMap)
  }

  override def close(): Unit = {
    getLogger(this.getClass).info("\n:::: The streaming of tweets has been stopped! ::::")
  }


  private def parseTweetAndStoreStats(tweet: String): Unit = {
    technologies.foreach { technology =>
      if (tweet.indexOf(technology) >= 0) {
        technologyCounterMap.update(technology, technologyCounterMap(technology) + 1L)
      }
    }
  }

}


class TweetStreamProcessorSupplier @Inject()(tweeterKafkaProcessor: TweeterKafkaProcessor) extends ProcessorSupplier[String, String] {
  override def get(): Processor[String, String] = tweeterKafkaProcessor
}