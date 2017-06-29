package com.knoldus.utils

import com.google.inject.ImplementedBy
import play.api.libs.json.{JsObject, JsString, JsValue, Json}

@ImplementedBy(classOf[JsonResponseImpl])
trait JsonResponse {

   def successResponse(data: JsValue, accessTokenOpt: Option[JsString]): JsObject = {
    if (accessTokenOpt.isDefined) {
      Json.obj("data" -> data, "accessToken" -> accessTokenOpt.fold(JsString(""))(identity))
    } else {
      Json.obj("data" -> data)
    }
  }

  def failureResponse(error: String): JsObject = {
    Json.obj("error" -> Json.obj("message" -> error))
  }

}

object JsonResponse extends JsonResponse

class JsonResponseImpl extends JsonResponse
