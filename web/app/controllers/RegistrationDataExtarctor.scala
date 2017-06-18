package controllers

import play.api.data._
import play.api.data.Forms._
import java.util.regex.{Matcher, Pattern}

case class UserFormData(
                         firstName: String,
                         middleName: Option[String], //Optional field
                         lastName: String,
                         userName: String,
                         password: String,
                         confirmPassword: String,
                         email: String,
                         slackId: Option[String],   //Optional field (for notification)
                         phone: Option[String]     //Optional field (for notification)
                       )

private[controllers] trait FormDataCreator extends {

  val userFormWithConstraints = Form(
    mapping(
      "first-name" -> nonEmptyText,
      "middleName" -> optional(nonEmptyText),
      "lastName" -> nonEmptyText,
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText,
      "confirmPassword" -> nonEmptyText,
      "email" -> nonEmptyText,
      "slackId" -> optional(nonEmptyText),
      "phone" -> optional(nonEmptyText)
    )(UserFormData.apply)(UserFormData.unapply)
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
                         phone: Option[String]): Option[UserFormData] = {

    isAlphabeticWithNoSpaces(firstName) &
    isAlphabeticWithNoSpaces(middleName.getOrElse("")) &
    isAlphabeticWithNoSpaces(lastName) &
    isUserName(userName) &
    passwordsMatch(password, confirmPassword) &
    isEmail(email) &
    isAlphaNumericWithNoSpaces(slackId.getOrElse("")) &
    isPhoneNumber(phone.getOrElse("")) match {
      case true => Some(UserFormData(firstName, middleName, lastName, userName, password, confirmPassword, email, slackId, phone))
      case false => None
    }

  }

  def isUserName(string: String): Boolean = {
    val pattern : String = "([a-zA-Z0-9_\\-\\.@#\\$]+)"
    verifyRegex(pattern, string)
  }

  def passwordsMatch(pass1: String, pass2: String): Boolean = {
    pass1 == pass2
  }

  def isAlphaNumericWithNoSpaces(string: String): Boolean = {
    val pattern : String = "([a-zA-Z0-9]{10})"
    verifyRegex(pattern, string)
  }

  def isPhoneNumber(string: String): Boolean = {
    val pattern : String = "([0-9]{10})"
    verifyRegex(pattern, string)
  }

  def isAlphabeticWithNoSpaces(string: String): Boolean = {
    val pattern: String = "([a-zA-Z]+)"
    verifyRegex(pattern, string)
  }

  def isEmail(string: String): Boolean = {
    val emailPattern: String = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
    verifyRegex(emailPattern, string)
  }

  def verifyRegex(regexPattern: String, stringToMatch: String): Boolean = {
    val pattern: Pattern = Pattern.compile(regexPattern)
    val matcher: Matcher = pattern.matcher(stringToMatch)
    matcher.matches()
  }

}
