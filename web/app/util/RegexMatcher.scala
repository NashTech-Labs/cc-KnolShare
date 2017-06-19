package util

import java.util.regex.{Matcher, Pattern}

/**
  * Created by knoldus on 19/6/17.
  */
trait RegexMatcher {

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

  private[util] def verifyRegex(regexPattern: String, stringToMatch: String): Boolean = {
    val pattern: Pattern = Pattern.compile(regexPattern)
    val matcher: Matcher = pattern.matcher(stringToMatch)
    matcher.matches()
  }

}
