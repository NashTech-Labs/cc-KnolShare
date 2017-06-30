package controllers

import javax.inject.Inject

import scala.concurrent.Future

import com.knoldus.exceptions.PSqlException.{InsertionError, UserNotFoundException}
import com.knoldus.models.{User, UserResponse}
import com.knoldus.utils.JsonResponse
import controllers.security.SecuredAction
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller, Request, Result}
import service.UserService
import userHelper.{Helper, PassWordUtility}


class UserController @Inject()(
    val userService: UserService,
    val passWordUtility: PassWordUtility, accessTokenHelper: Helper, jsonResponse: JsonResponse)
  extends Controller with SecuredAction {

  def registerUser: Action[AnyContent] = {
    Action.async {
      implicit request =>
        val (userEmail: String, phoneNumber: String, password: String, confirmPassword: String,
        userName: String, listFields: List[String]) = extractJsonFromRequest(
          request)
        createUserJson(userEmail, phoneNumber, password, confirmPassword, userName, listFields)
    }
  }

  private def createUserJson(userEmail: String,
      phoneNumber: String,
      password: String,
      confirmPassword: String,
      userName: String,
      listFields: List[String]): Future[Result] = {
    if (validateFields(listFields)) {
      if (userService.validatePassWord(password, confirmPassword)) {
        createSuccessResponseJson(userEmail, phoneNumber, password, userName)
      }
      else {
        Future(NotFound(
          jsonResponse.failureResponse("password and confirm password do not match ")))
      }
    }
    else {
      createFailureResponseJson
    }
  }

  private def createFailureResponseJson: Future[Result] = {
    Future(BadRequest(
      jsonResponse.failureResponse("wrong json content ")))
  }

  private def createSuccessResponseJson(userEmail: String,
      phoneNumber: String,
      password: String,
      userName: String): Future[Result] = {
    val user = createUserFromJson(userEmail.toLowerCase, phoneNumber, password, userName)
    val userResponse = UserResponse(user.userName, user.email, user.phoneNumber)
    userService.createUser(user).flatMap {
      _ => {
        userService
          .sendMail(List(userEmail),
            "Confirm your Registration",
            "Congratulations !! \nYou have successfully completed your registration process")

        val accessToken = accessTokenHelper.generateAccessToken
        Future.successful(Ok(
          jsonResponse.successResponse(Json.obj("user" -> userResponse.toJson, "accessToken" -> JsString(accessToken))))
          .withSession("accessToken" -> accessToken))
      }
    }.recover {
      case insertionError: InsertionError => BadRequest(jsonResponse.failureResponse(insertionError.message))
    }
  }

  private def createUserFromJson(userEmail: String,
      phoneNumber: String,
      password: String,
      userName: String): User = {
    User(None,
      userName,
      userEmail,
      passWordUtility.hashedPassword(password),
      phoneNumber)
  }

  private def validateFields(fields: List[String]): Boolean = fields.forall(_.nonEmpty)

  private def extractJsonFromRequest(request: Request[AnyContent]): (String, String, String,
    String, String,
    List[String]) = {

    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
    val phoneNumber = (bodyJs \ "phoneNumber").asOpt[String].fold("")(identity)
    val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
    val confirmPassword = (bodyJs \ "confirmPassword").asOpt[String].fold("")(identity)
    val userName = (bodyJs \ "userName").asOpt[String].fold("")(identity)
    val listFields = List(userEmail,
      userName,
      userEmail,
      password,
      confirmPassword,
      phoneNumber)
    (userEmail, phoneNumber, password, confirmPassword, userName, listFields)
  }

  def validateLogin: Action[AnyContent] = {
    Action.async {
      implicit request =>
        val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
        val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
        val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
        userService.validateUser(userEmail).flatMap {
          user => {
            if (passWordUtility.verifyPassword(password, user.password)) {
              val accessToken = accessTokenHelper.generateAccessToken
              Future.successful(Ok(
                jsonResponse.successResponse(Json.obj("user" -> UserResponse(user.userName, user.email, user.phoneNumber).toJson,
                  "accessToken" -> JsString(accessToken)))).withSession("accessToken" -> accessToken))
            }
            else {
              Future(NotFound(
                jsonResponse.failureResponse("Invalid UserName or Password")))
            }
          }
        }.recover {
          case userNotFoundException: UserNotFoundException => BadRequest(jsonResponse.failureResponse(
            userNotFoundException.message))
        }

    }
  }
}



