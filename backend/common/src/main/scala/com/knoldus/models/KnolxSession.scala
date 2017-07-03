package com.knoldus.models

import java.sql.Date
import com.knoldus.utils.JsonHelper.knolxSessionFormat
import play.api.libs.json.Json


case class KnolxSession(id: Int,
                        presentor: String,
                        topic: Option[String],
                        session_id: Option[Int],
                        rating: Option[Int],
                        scheduledDate: Date){
  lazy val toJson = Json.toJson(this)
}
