package UserHelper

import scala.util.Random

import com.knoldus.utils.Constants


object Helper {

  def generateAccessToken: String = Random.alphanumeric.take(Constants.TEN).mkString("")

  def validatePassWord(password: String, confirmPassword: String): Boolean = {
    password
      .equals(confirmPassword)
  }
}
