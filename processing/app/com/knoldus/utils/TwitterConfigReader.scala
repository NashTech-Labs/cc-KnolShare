package com.knoldus.utils

import com.typesafe.config.ConfigFactory

class TwitterConfigReader {

  val conf = ConfigFactory.load()

  def getTwitterConsumerKey() = conf.getString("twitter.consumerKey")

  def getTwitterConsumerSecretKey() = conf.getString("twitter.consumerSecret")

  def getTwitterAccessToken() = conf.getString("twitter.accessToken")

  def getTwitterAccessSecretToken() = conf.getString("twitter.accessTokenSecret")

  def getKafkaServers() = conf.getString("kafka.servers")

  def getKafkaTopic() = conf.getString("kafka.topic")

  def getKStreamTopic() = conf.getString("kstream.topic")

}
