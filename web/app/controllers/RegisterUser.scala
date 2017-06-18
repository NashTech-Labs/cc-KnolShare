package controllers

import models.User
import play.api.mvc.{Action, Controller}

class RegisterUser extends Controller with FormDataCreator {

  def register = Action { implicit request =>

    userFormWithConstraints.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      userData => {
        val newUser = User(userData.firstName, userData.middleName, userData.lastName, userData.userName,
          userData.password, userData.email, userData.slackId, userData.phone)
        val id = storeNewUserDetails(newUser)

        Redirect(routes.Application.index)
          .withSession("connectedSession"-> userData.email)
      }
    )

  }

  //After the persistence module is done --- It'll be processed further
  private[this] def storeNewUserDetails(user: User): Int = ???

}
