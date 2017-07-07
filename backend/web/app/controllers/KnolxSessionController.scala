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

class KnolxSessionController @Inject()(knolxSessionService: KnolxSessionService) extends
  Controller with SecuredAction with LoggerHelper with JsonResponse {

  def createKnolxSession: Action[AnyContent] = UserAction.async { implicit request =>
    val (presenter: String, topic: String, sessionId: Int, rating: Int, scheduledDate: Date) =
      extractJsonFromRequest

    val topicProvided: Option[String] = if (topic.isEmpty) None else { Some(topic) }
    val (knolxSessionId, knolxRating) = getSessionData(sessionId, rating)
    val knolxSession = KnolxSession(-1, presenter, topicProvided, knolxSessionId, knolxRating,
      scheduledDate)
    knolxSessionService.createKnolxSession(knolxSession).map { knolxSession =>
      Ok(successResponse(Json.obj("message" -> JsString("Knolx session successfully registered"), "knolx" -> knolxSession.toJson)))
    } recover {
      case NonFatal(ex) => {
        getLogger(this.getClass).error(s"\n\n ${ex.getMessage}")
        BadRequest(failureResponse("Internal Server Error"))
      }
    }
  }

  private def extractJsonFromRequest()(implicit request: SecuredRequest[AnyContent]):
  (String, String, Int, Int, Date) = {
    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val presenter = (bodyJs \ "presenter").asOpt[String].fold("")(identity)
    val topic = (bodyJs \ "topic").asOpt[String].fold("")(identity)
    val sessionId = (bodyJs \ "sessionId").asOpt[String].fold(-1)(id => id.toInt)
    val rating = (bodyJs \ "rating").asOpt[String].fold(-1)(rating => rating.toInt)
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
      Ok(successResponse(Json.obj("sessionList" -> Json.toJson(sessionList))))
    }) recover {
      case NonFatal(ex) => {
        getLogger(this.getClass).error(s"\n\n ${ex.getMessage}")
        BadRequest(failureResponse("Internal Server Error"))      }
    }
  }

}
