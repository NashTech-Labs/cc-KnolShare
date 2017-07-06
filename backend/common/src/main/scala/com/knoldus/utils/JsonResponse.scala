package com.knoldus.utils

import com.google.inject.ImplementedBy
import play.api.libs.json.{JsObject, JsString, JsValue, Json}

@ImplementedBy(classOf[JsonResponseImpl])
trait JsonResponse {

   def successResponse(data: JsValue): JsObject = {
      Json.obj("data" -> data)
  }

  def failureResponse(error: String): JsObject = {
    Json.obj("error" -> Json.obj("message" -> error))
  }

}

object JsonResponse extends JsonResponse

class JsonResponseImpl extends JsonResponse
