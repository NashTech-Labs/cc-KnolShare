package com.knoldus.utils

import com.knoldus.models.{KnolxSession, UserResponse}
import play.api.libs.json.Json

object JsonHelper {

  implicit val userFormat = Json.format[UserResponse]
  implicit val knolxSessionFormat = Json.format[KnolxSession]
}
