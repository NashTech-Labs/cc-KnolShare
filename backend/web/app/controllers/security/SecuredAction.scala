package controllers.security

import scala.concurrent.Future

import play.api.mvc.{ActionBuilder, Controller, Request, Result, WrappedRequest}
import service.UserService

case class UserSession(accessToken: String, email: String)

case class SecuredRequest[A](accessToken: String, request: Request[A])
  extends WrappedRequest(request)

trait SecuredAction {

  self: Controller =>

  val userService: UserService

  object UserAction extends ActionBuilder[SecuredRequest] {

    override def invokeBlock[A](request: Request[A],
        block: (SecuredRequest[A]) => Future[Result]): Future[Result] = {
      implicit val req = request
      val accessToken = req.headers.get("accessToken").fold("")(identity)
      if (req.session.get("accessToken").fold("")(identity).equals(accessToken) && !accessToken.isEmpty) {
        block(SecuredRequest(accessToken, request))
      } else {
        Future.successful(Unauthorized("Unauthorized Access !!"))
      }
    }
  }

}
