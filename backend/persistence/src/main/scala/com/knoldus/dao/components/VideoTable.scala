package com.knoldus.dao.components

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.VideoStore
import slick.lifted.ProvenShape

trait VideoTable {
  this: DBComponent =>

  import driver.api._

  private[components] class VideoTable(tag: Tag) extends Table[VideoStore](tag, "video_store") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val presentor = column[String]("presentor")
    val topic = column[String]("topic")
    val videoUrl = column[String]("video_url")
    val rating = column[Int]("rating")

    def * : ProvenShape[VideoStore] = (id, presentor, topic, videoUrl, rating) <> ((VideoStore.apply _).tupled, VideoStore.unapply)
  }

  val videoTableQuery = TableQuery[VideoTable]
}
