package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.VideoStore
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


trait VideoStoreComponent extends VideoTable {

  this: DBComponent =>

  import driver.api._

  def addVideo(video: VideoStore): Future[VideoStore] = {
    db.run {
      videoTableQuery returning videoTableQuery.map(_.id) += video
    }.map(videoId => video.copy(id = videoId))
  }

  def getvideoByTopic(topic: String): Future[Option[VideoStore]] = {
    db.run(videoTableQuery.filter(_.topic === topic).result.headOption)
  }

  def getVideoByPresentor(presentor: String): Future[Option[VideoStore]] = {
    db.run(videoTableQuery.filter(_.presentor === presentor).result.headOption)
  }

}
