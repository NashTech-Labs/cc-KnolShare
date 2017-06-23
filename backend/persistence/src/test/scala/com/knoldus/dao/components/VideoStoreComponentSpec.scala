package com.knoldus.dao.components

import com.knoldus.utils.Constants.RATING
import com.knoldus.dao.connection.TestDBHelper
import com.knoldus.models.VideoStore
import play.api.test.PlaySpecification

class VideoStoreComponentSpec extends PlaySpecification with VideoStoreComponent with TestDBHelper {

  sequential
  "Video Store Component" should {

    "be able to add video to video store" in {
      val video = await(addVideo(VideoStore(2, "Shivangi", "abc", "Some_Url", RATING)))
      video.id must be equalTo 2
    }


    "be able to get video by topic's name" in {
      val video = await(getvideoByTopic("xyz"))
      video.get.id must beEqualTo(1)
    }

    "be able to get video by presentor's name" in {
      val video = await(getVideoByPresentor("Sangeeta"))
      video.get.id must beEqualTo(1)
    }

  }
}
