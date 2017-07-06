package com.knoldus.utils

import java.sql.Date

import com.knoldus.models.{KnolxSession, UserResponse}
import play.api.libs.json.Json.{fromJson, toJson}
import play.api.libs.json._

object JsonHelper extends LoggerHelper {

  def dateToJsvalue(date: Date): JsString = {
    JsString(date.toString)
  }

  implicit val dateFormat = new Format[Date] {
    override def reads(json: JsValue): JsResult[Date] = {
        getLogger(this.getClass).info("Mapping Json for Date to Case Class : Reads")
        fromJson[Date](json)
      }
    override def writes(date: Date): JsValue = {
      getLogger(this.getClass).info("Mapping Json for Date to Case Class : Write")
      toJson(dateToJsvalue(date))
    }
  }

  implicit val userFormat = Json.format[UserResponse]
  implicit val knolxSessionFormat = Json.format[KnolxSession]
}
