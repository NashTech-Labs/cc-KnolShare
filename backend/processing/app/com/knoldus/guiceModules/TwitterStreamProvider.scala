package com.knoldus.guiceModules

import javax.inject.Inject

import com.google.inject.Provider
import com.knoldus.utils.TwitterConfigReader
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{TwitterStream, TwitterStreamFactory}


class TwitterStreamProvider @Inject()(configReader: TwitterConfigReader, twitterConfigBuilder: ConfigurationBuilder)
  extends Provider[TwitterStream] {

  override def get(): TwitterStream = {
    val twitterConf = twitterConfigBuilder.setOAuthConsumerKey(configReader.getTwitterConsumerKey)
      .setOAuthConsumerSecret(configReader.getTwitterConsumerSecretKey)
      .setOAuthAccessToken(configReader.getTwitterAccessToken)
      .setOAuthAccessTokenSecret(configReader.getTwitterAccessSecretToken).build
    new TwitterStreamFactory(twitterConf).getInstance
  }
}
