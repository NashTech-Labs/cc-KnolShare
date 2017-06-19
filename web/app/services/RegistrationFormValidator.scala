package services

import play.api.data.Forms._
import play.api.data._
import util.RegexMatcher

case class RegistrationFormData(
                         firstName: String,
                         middleName: Option[String],//Optional field
                         lastName: String,
                         userName: String,
                         password: String,
                         confirmPassword: String,
                         email: String,
                         slackId: Option[String],   //Optional field (for notification)
                         phone: Option[String]      //Optional field (for notification)
                       )

trait RegistrationFormValidator extends RegexMatcher{

  val userFormWithConstraints = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "middleName" -> optional(nonEmptyText),
      "lastName" -> nonEmptyText,
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText,
      "confirmPassword" -> nonEmptyText,
      "email" -> nonEmptyText,
      "slackId" -> optional(nonEmptyText),
      "phone" -> optional(nonEmptyText)
    )(RegistrationFormData.apply)(RegistrationFormData.unapply)
        .verifying("Incorrect form fields!!", { userData =>
          validateFormFields(userData.firstName, userData.middleName, userData.lastName, userData.userName,
            userData.password, userData.confirmPassword, userData.email, userData.slackId, userData.phone).isDefined
        })
  )

  def validateFormFields(firstName: String,
                         middleName: Option[String],
                         lastName: String,
                         userName: String,
                         password: String,
                         confirmPassword: String,
                         email: String,
                         slackId: Option[String],
                         phone: Option[String]): Option[RegistrationFormData] = {

    isAlphabeticWithNoSpaces(firstName) &
    isAlphabeticWithNoSpaces(middleName.getOrElse("")) &
    isAlphabeticWithNoSpaces(lastName) &
    isUserName(userName) &
    passwordsMatch(password, confirmPassword) &
    isEmail(email) &
    isAlphaNumericWithNoSpaces(slackId.getOrElse("")) &
    isPhoneNumber(phone.getOrElse("")) match {
      case true => Some(RegistrationFormData(firstName, middleName, lastName, userName, password, confirmPassword, email, slackId, phone))
      case false => None
    }
  }
}

object RegistrationFormValidatorImpl extends RegistrationFormValidator
