package controllers.security

import com.knoldus.dao.services.user.UserSessionDBService
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class SecuredRequest[A](accessToken: String, request: Request[A])
  extends WrappedRequest(request)

trait SecuredAction {

  object UserAction extends ActionBuilder[SecuredRequest] with Controller {

    val userSessionService = UserSessionDBService

    override def invokeBlock[A](request: Request[A],
                                block: (SecuredRequest[A]) => Future[Result]): Future[Result] = {
      val accessToken = request.headers.get("accessToken").fold("")(identity)
      val email = request.headers.get("email").fold("")(identity)
      userSessionService.getUserSessionByEmail(email).flatMap {
        userSessionOpt =>
          userSessionOpt.fold(Future.successful(Unauthorized("User Does not Exist !!"))) { userSession =>
            if (userSession.accessToken.equals(accessToken)) {
              block(SecuredRequest(accessToken, request))
            } else {
              Future.successful(Unauthorized("Unauthorized Access !!"))
            }
          }
      }
    }
  }

}
