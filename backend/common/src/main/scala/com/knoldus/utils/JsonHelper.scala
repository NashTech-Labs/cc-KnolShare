package com.knoldus.utils

import com.knoldus.models.UserResponse
import play.api.libs.json.Json

object JsonHelper {

  implicit val userFormat = Json.format[UserResponse]
}
