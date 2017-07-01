package controllers

import scala.concurrent.Future

import com.google.inject.Inject
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.utils.JsonResponse
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import service.AdminService
import userHelper.{Helper, PassWordUtility}


class AdminController @Inject()(adminService: AdminService,
    passWordUtility: PassWordUtility,
    accessTokenHelper: Helper,
    jsonResponse: JsonResponse) extends Controller {

  def validateLogin: Action[AnyContent] = {
    Action.async {
      implicit request =>
        val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
        val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
        val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
        adminService.validateUser(userEmail).flatMap {
          admin => {
            if (passWordUtility.verifyPassword(password, admin.password)) {
              val accessToken = accessTokenHelper.generateAccessToken
              Future.successful(Ok(
                jsonResponse.successResponse(Json.obj("admin" -> Json.obj("email"->JsString(admin.email)), "accessToken" -> JsString(accessToken))))
                .withSession("accessToken" -> accessToken))
            }
            else {
              Future(NotFound(
                jsonResponse.failureResponse("Invalid UserName or Password")))
            }
          }
        }.recover {
          case userNotFoundException: UserNotFoundException => BadRequest(jsonResponse
            .failureResponse(
              userNotFoundException.message))
        }
    }
  }
}
