package services


import java.util.regex.{Matcher, Pattern}

import authentication.UserAuthenticator
import play.api.data.Forms._
import play.api.data._
import util.RegexMatcher

case class LoginFormData(
                        userName: String,
                        password: String
                        )

trait LoginFormValidator extends UserAuthenticator with RegexMatcher{

  val loginFormWithConstraints = Form(
    mapping(
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginFormData.apply)(LoginFormData.unapply)
      .verifying("Incorrect form fields!!", { userData =>
        verifyLoginForm(userData.userName, userData.password).isDefined
      })
  )

  def verifyLoginForm(userName: String, password: String): Option[LoginFormData] = {
    if(isUserName(userName) && authenticate(userName, password)) Some(LoginFormData(userName, password))
      else None
  }

}

object LoginFormValidatorImpl extends LoginFormValidator
