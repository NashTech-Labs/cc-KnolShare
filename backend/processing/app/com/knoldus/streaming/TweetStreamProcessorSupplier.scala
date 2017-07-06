package com.knoldus.streaming

import com.knoldus.utils.{Constants, LoggerHelper}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier}
import org.apache.kafka.streams.state.KeyValueStore

object MyProcessor extends Processor[String, String] with LoggerHelper {

  var context: ProcessorContext = null
  var kvStore: KeyValueStore[String, String] = null

  override def init(processorContext: ProcessorContext): Unit = {
    this.context = processorContext
    this.kvStore = context.getStateStore("stateStore").asInstanceOf[KeyValueStore[String, String]]
    this.context.schedule(Constants.THOUSAND)
  }

  override def process(key: String, tweet: String): Unit = {
    this.kvStore.put(tweet, tweet)
    this.context.commit()
  }

  override def punctuate(l: Long): Unit = {

    kvStore

    val iter = kvStore.all

    while (iter.hasNext) {
      val entry = iter.next()
      getLogger(this.getClass).info("Tweet -> Key: " + entry.key + ", " + "Value: " + entry.value)
      this.context.forward(entry.key, entry.value.toString)

    }
  }

  override def close(): Unit = {
  }
}

class TweetStreamProcessorSupplier extends ProcessorSupplier[String, String] {
  override def get(): Processor[String, String] = {
    MyProcessor
  }
}
