package controllers

import javax.inject.Inject

import scala.concurrent.Future

import UserHelper.{Helper, PassWordUtility}
import com.knoldus.models.{User, UserResponse}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller, Request, Result}
import service.UserService


class UserController @Inject()(val cache: CacheApi, val userService: UserService)
  extends Controller {

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
          failureResponse("password and confirm password do not match ")))
      }
    }
    else {
      createfailureResponseJson
    }
  }

  private def createfailureResponseJson: Future[Result] = {
    Future(BadRequest(
      failureResponse("wrong json content ")))
  }

  private def createSuccessResponseJson(userEmail: String,
      phoneNumber: String,
      password: String,
      userName: String): Future[Result] = {
    val accessToken = Helper.generateAccessToken
    cache.set(userEmail, accessToken)
    val user: User = createUserFromJson(userEmail.toLowerCase, phoneNumber, password, userName)
    val userResponse = UserResponse(user.userName, user.email, user.phoneNumber)
    userService.createUser(user).flatMap {
      user => {
        userService.sendMail(List(userEmail), "Confirm your Registration", "Click below " +
                                                                           "to confirm user " +
                                                                           "registration:\nhttp://www.realaddressgoeshere.com/registerer/" +
                                                                           "activateuser?token=sometokengoesher")
        Future.successful(Ok(
          successResponse(Json.toJson(userResponse))))
      }
    }
  }

  private def createUserFromJson(userEmail: String,
      phoneNumber: String,
      password: String,
      userName: String): User = {
    User(None,
      userName,
      userEmail,
      PassWordUtility.hashedPassword(password),
      phoneNumber)
  }

  private def successResponse(data: JsValue) = {
    Json.obj("data" -> data)
  }

  private def validateFields(fields: List[String]): Boolean = fields.forall(_.nonEmpty)

  private def failureResponse(error: String) = {
    Json.obj("error" -> Json.obj("message" -> error))
  }

  private def extractJsonFromRequest(request: Request[AnyContent]): (String, String, String,
    String, String,
    List[String]) = {
    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val userEmail = (bodyJs \ "email").asOpt[String].getOrElse("")
    val phoneNumber = (bodyJs \ "phoneNumber").asOpt[String].getOrElse("")
    val password = (bodyJs \ "password").asOpt[String].getOrElse("")
    val confirmPassword = (bodyJs \ "confirmPassword").asOpt[String].getOrElse("")
    val userName = (bodyJs \ "userName").asOpt[String].getOrElse("")
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
        val userEmail = (bodyJs \ "email").asOpt[String].getOrElse("")
        val password = (bodyJs \ "password").asOpt[String].getOrElse("")
        userService.validateUser(userEmail).flatMap {
          user => {
            if (PassWordUtility.verifyPassword(password, user.password)) {
              Future.successful(Ok(
                successResponse(Json
                  .toJson(UserResponse(user.userName, user.email, user.phoneNumber)))))
            }
            else {
              Future(NotFound(
                failureResponse("Invalid UserName or Password")))
            }
          }
        }

    }
  }
}



