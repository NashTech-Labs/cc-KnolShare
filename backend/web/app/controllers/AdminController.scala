package controllers

import scala.concurrent.Future
import com.google.inject.Inject
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.models.{Admin, User, UserResponse, UserSession}
import com.knoldus.utils.JsonResponse
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import service.{AdminService, UserSessionService}
import userHelper.{Helper, PassWordUtility}


class AdminController @Inject()(adminService: AdminService,
    passWordUtility: PassWordUtility,
    accessTokenHelper: Helper,
    jsonResponse: JsonResponse, userSessionService: UserSessionService) extends Controller {

  def validateLogin: Action[AnyContent] = {
    Action.async {
      implicit request =>
        val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
        val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
        val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
        val accessToken = accessTokenHelper.generateAccessToken
        val userFut: Future[(Admin, Boolean)] = for {
        user <- adminService.validateUser(userEmail)
        isUser = passWordUtility.verifyPassword(password, user.password)
      } yield (user,isUser)
      userFut.flatMap {
        case (user, true) => userSessionService.createUserSession(UserSession(None, userEmail, accessToken)).map { _ =>
          Ok(jsonResponse.successResponse(Json.obj("admin" -> Json.obj("email" -> JsString(user.email)), "accessToken" -> JsString(accessToken))))
            .withSession("accessToken" -> accessToken)
        }
        case _ => Future(NotFound(jsonResponse.failureResponse("Invalid UserName or Password")))
      }.recover {
        case userNotFoundException: UserNotFoundException => BadRequest(jsonResponse.failureResponse(userNotFoundException.message))
      }
    }
  }
}
