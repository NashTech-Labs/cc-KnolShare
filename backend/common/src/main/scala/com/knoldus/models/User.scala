package com.knoldus.models

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
)
object UserResponse {
  implicit val userFormat = Json.format[UserResponse]
}