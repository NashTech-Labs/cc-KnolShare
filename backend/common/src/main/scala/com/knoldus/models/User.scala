package com.knoldus.models

import com.knoldus.utils.JsonHelper._
import play.api.libs.json.Json


case class User(
    id: Option[Int] = None,
    userName: String,
    email: String,
    password: String,
    phoneNumber: String
)


case class UserResponse(
    userName: String,
    email: String,
    phoneNumber: String
) {
  lazy val toJson = Json.toJson(this)
}
