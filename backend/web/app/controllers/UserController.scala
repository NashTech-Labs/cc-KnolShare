package controllers

import javax.inject.Inject

import com.knoldus.dao.services.user.UserSessionDBService
import com.knoldus.exceptions.NotificationException.MailerDaemonException
import com.knoldus.exceptions.PSqlException.{DatabaseException, InsertionError, UserNotFoundException}
import com.knoldus.models.{User, UserResponse, UserSession}
import com.knoldus.utils.{Constants, JsonResponse, LoggerHelper}
import controllers.security.SecuredAction
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}
import play.api.mvc._
import service.{UserService, UserSessionService}
import userHelper.{Helper, PassWordUtility}

import scala.concurrent.Future

class UserController @Inject()(val userService: UserService, val passWordUtility: PassWordUtility,accessTokenHelper: Helper,
                               jsonResponse: JsonResponse, userSessionService: UserSessionService, val userSessionServ: UserSessionDBService)
  extends Controller with SecuredAction[UserSessionDBService] with LoggerHelper {

  def registerUser: Action[AnyContent] = Action.async { implicit request =>
    val (userEmail: String, phoneNumber: String, password: String, confirmPassword: String,
    userName: String, listFields: List[String]) = extractJsonFromRequest(request)
    createUserJson(userEmail, phoneNumber, password, confirmPassword, userName, listFields)
  }

  private def createUserJson(userEmail: String, phoneNumber: String, password: String, confirmPassword: String,
                             userName: String, listFields: List[String]): Future[Result] = {
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

  private def createSuccessResponseJson(userEmail: String, phoneNumber: String, password: String,
                                        userName: String): Future[Result] = {
    val user = createUserFromJson(userEmail.toLowerCase, phoneNumber, password, userName)
    val userResponse = UserResponse(user.userName, user.email, user.phoneNumber)
    val accessToken = accessTokenHelper.generateAccessToken
    val userFut: Future[User] = for {
      user: User <- userService.createUser(user)
      userSession <- userSessionService.createUserSession(UserSession(None, userEmail, accessToken))
    } yield user
    userFut.map { _ => {
      userService.sendMail(List(userEmail), Constants.MAIL_SUBJECT, Constants.MAIL_BODY).recover {
        case mailerDaemonError: MailerDaemonException =>
          getLogger(this.getClass).error("Unable to send Confirmation Mail : ")
      }
      Ok(jsonResponse.successResponse(Json.obj("user" -> userResponse.toJson,
        "accessToken" -> JsString(accessToken)))).withSession("accessToken" -> accessToken)
    }
    }.recover {
      case insertionError: InsertionError => BadRequest(jsonResponse
        .failureResponse(insertionError.message))
    }
  }

  private def createUserFromJson(userEmail: String, phoneNumber: String, password: String,
                                 userName: String): User = {
    User(None, userName, userEmail, passWordUtility.hashedPassword(password), phoneNumber)
  }

  private def validateFields(fields: List[String]): Boolean = fields.forall(_.nonEmpty)

  private def extractJsonFromRequest(request: Request[AnyContent]) = {
    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
    val phoneNumber = (bodyJs \ "phoneNumber").asOpt[String].fold("")(identity)
    val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
    val confirmPassword = (bodyJs \ "confirmPassword").asOpt[String].fold("")(identity)
    val userName = (bodyJs \ "userName").asOpt[String].fold("")(identity)
    val listFields = List(userEmail, userName, userEmail, password, confirmPassword, phoneNumber)
    (userEmail, phoneNumber, password, confirmPassword, userName, listFields)
  }

  def validateLogin: Action[AnyContent] = Action.async { implicit request =>
    val bodyJs = request.body.asJson.getOrElse(Json.parse(""))
    val userEmail = (bodyJs \ "email").asOpt[String].fold("")(identity)
    val password = (bodyJs \ "password").asOpt[String].fold("")(identity)
    val accessToken = accessTokenHelper.generateAccessToken
    val userFut: Future[(User, Boolean)] = for {
      user <- userService.validateUser(userEmail)
      isUser = passWordUtility.verifyPassword(password, user.password)
    } yield (user,isUser)
    userFut.flatMap {
      case (user, true) => userSessionService.createUserSession(UserSession(None, userEmail, accessToken)).map { _ =>
          Ok(jsonResponse.successResponse(Json.obj("user" -> UserResponse(user.userName, user.email, user.phoneNumber).toJson,
                "accessToken" -> JsString(accessToken)))).withSession("access_token" -> accessToken)
        }
      case _ => Future(NotFound(jsonResponse.failureResponse("Invalid UserName or Password")))
    }.recover {
      case userNotFoundException: UserNotFoundException => BadRequest(jsonResponse.failureResponse(userNotFoundException.message))
    }
  }

  def logout: Action[AnyContent] = UserAction.async { implicit request =>

    userSessionService.deleteUserSessionByEmail(request.headers.get("email").fold("")(identity)).map { _ =>
     Ok(jsonResponse.successResponse(Json.obj("message" -> JsString("User Logged Out successfully !!")))).withNewSession
    }. recover {
            case databaseException: DatabaseException => BadRequest(jsonResponse.failureResponse(databaseException.message))
    }
  }
}



