package controllers.security

import play.api.libs.json.Json

import scala.concurrent.Future
import play.api.mvc.{ActionBuilder, Controller, Request, Result, WrappedRequest}

case class UserSession(accessToken: String, email: String)

case class SecuredRequest[A](accessToken: String, request: Request[A])
  extends WrappedRequest(request)

trait SecuredAction {

  self: Controller =>

  object UserAction extends ActionBuilder[SecuredRequest] {

    override def invokeBlock[A](request: Request[A],
        block: (SecuredRequest[A]) => Future[Result]): Future[Result] = {
      implicit val req = request
      val accessToken = req.headers.get("accessToken").fold("")(identity)
      if (req.session.get("accessToken").fold("")(identity).equals(accessToken) && !accessToken.isEmpty) {
        block(SecuredRequest(accessToken, request))
      } else {
        Future.successful(BadRequest(Json.obj("error" -> Json.obj("message" -> "Unauthorised Access")))
      )
      }
    }
  }

}
