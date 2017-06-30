package userHelper

import scala.util.Random

import com.google.inject.ImplementedBy
import com.knoldus.utils.Constants

@ImplementedBy(classOf[HelperImpl])
trait Helper {

  def generateAccessToken: String = Random.alphanumeric.take(Constants.TEN).mkString("")

  def validatePassWord(password: String, confirmPassword: String): Boolean = {
    password
      .equals(confirmPassword)
  }
}

class HelperImpl extends Helper

object Helper extends Helper
