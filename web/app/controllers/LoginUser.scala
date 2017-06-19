package controllers

import play.api.mvc.{Action, Controller}
import services.{LoginFormValidator, LoginFormValidatorImpl}

/**
  * Created by knoldus on 19/6/17.
  */
class LoginUser extends Controller{

  val loginFormValidator: LoginFormValidator = LoginFormValidatorImpl

  def login = Action { implicit request =>

    loginFormValidator.loginFormWithConstraints.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      },
      userData => {
        val userName = userData.userName
        val randomStringAsSessionKey = scala.util.Random.nextString(20)
        Redirect(routes.Application.index)
            .withSession("connectedSession" -> userData.userName, "sesstionKey" -> randomStringAsSessionKey)
      }
    )
  }



}
