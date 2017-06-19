package controllers

import models.User
import play.api.mvc.{Action, Controller}
import services.{RegistrationFormValidator, RegistrationFormValidatorImpl}
import com.github.t3hnar.bcrypt._

class RegisterUser extends Controller {

  val registrationFormValidator: RegistrationFormValidator = RegistrationFormValidatorImpl

  def register = Action { implicit request =>

    registrationFormValidator.userFormWithConstraints.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      userData => {
        val newUser = User(userData.firstName, userData.middleName, userData.lastName, userData.userName,
          userData.password.bcrypt, userData.email, userData.slackId, userData.phone)
        val id = storeNewUserDetails(newUser)
        /*--- Call the the mail verification method here to verify user registration ----*/
        Redirect(routes.Application.index())
          .withSession("connectedSession"-> userData.email)
      }
    )

  }

  //After the persistence module is done --- It'll be processed further
  private[this] def storeNewUserDetails(user: User): Int = ???

}
