package com.knoldus.streaming

import com.google.inject.AbstractModule

class Launcher extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[TweetLauncher]).asEagerSingleton()
  }
}
