package userHelper

import org.mindrot.jbcrypt.BCrypt

// $COVERAGE-OFF$

class PassWordUtility {

  def hashedPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def verifyPassword(plainPassword: String, hashedPassword: String): Boolean = {
    BCrypt.checkpw(plainPassword, hashedPassword)
  }
}

// $COVERAGE-ON$

