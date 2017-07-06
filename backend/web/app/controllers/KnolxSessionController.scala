package controllers

import java.sql.Date

import com.google.inject.Inject
import com.knoldus.models.KnolxSession
import com.knoldus.utils.{JsonResponse, LoggerHelper}
import security.{SecuredAction, SecuredRequest}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc.{Action, AnyContent, Controller, Request}
import service.KnolxSessionService
import com.knoldus.utils.JsonHelper._

import scala.util.control.NonFatal

class KnolxSessionController @Inject()(knolxSessionService: KnolxSessionService, jsonResponse: JsonResponse) extends
  Controller with SecuredAction with LoggerHelper {

  def createKnolxSession: Action[AnyContent] = UserAction.async { implicit request =>
    val (presenter: String, topic: String, sessionId: Int, rating: Int, scheduledDate: Date) =
      extractJsonFromRequest
    val topicProvided = topic match {
      case "" => None
      case _ => Some(topic)
    }

    val (knolxSessionId, knolxRating) = getSessionData(sessionId, rating)
    val knolxSession = KnolxSession(-1, presenter, topicProvided, knolxSessionId, knolxRating,
      scheduledDate)
    knolxSessionService.createKnolxSession(knolxSession).map { knolxSession =>
      Ok(jsonResponse.successResponse(Json.obj("message" -> JsString("Knolx session successfully registered"), "knolx" -> knolxSession.toJson)))
    } recover {
      case NonFatal(ex) => {
        getLogger(this.getClass).error(s"\n\n ${ex.getMessage}")
        BadRequest(jsonResponse.failureResponse("Internal Server Error"))
      }
    }
  }

  private def extractJsonFromRequest()(implicit request: SecuredRequest[AnyContent]):
  (String, String, Int, Int, Date) = {
    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val presenter = (bodyJs \ "presenter").asOpt[String].fold("")(identity)
    val topic = (bodyJs \ "topic").asOpt[String].fold("")(identity)
    val sessionId = (bodyJs \ "sessionId").asOpt[Int].fold(-1)(identity)
    val rating = (bodyJs \ "rating").asOpt[Int].fold(-1)(identity)
    val scheduledDate = (bodyJs \ "scheduledDate").asOpt[Date].fold(new Date(0,0,0))(identity)
    (presenter, topic, sessionId, rating, scheduledDate)
  }

  private def getSessionData(sessionId: Int, rating: Int): (Option[Int], Option[Int]) = {
    val knolxSessionId = sessionId match {
      case x => Some(x)
      case _ => None
    }
    val knolxRating = rating match {
      case x => Some(x)
      case _ => None
    }
    (knolxSessionId, knolxRating)
  }

  def getAllKnolxSession: Action[AnyContent] = UserAction.async { implicit request =>
    (knolxSessionService.getAllKnolxSession() map { sessionList =>
      Ok(jsonResponse.successResponse(Json.obj("sessionList" -> Json.toJson(sessionList))))
    }) recover {
      case NonFatal(ex) => {
        getLogger(this.getClass).error(s"\n\n ${ex.getMessage}")
        BadRequest(jsonResponse.failureResponse("Internal Server Error"))      }
    }
  }

}
