package authentication

import com.github.t3hnar.bcrypt._
import util.exception.UserNotExistsWithUsername


//TODO -- Delete this trait and use the method of persistence instead
trait DemoDB {
  def returnHashedPassForUserName(userName: String): Option[String] = ???

  /*
  * if record found in DBMS extract the hashed password from that row and match with the password given
  * */

}

trait UserAuthenticator extends DemoDB{
  def authenticate(userName: String, password: String): Boolean = {
    returnHashedPassForUserName(userName) match {
      case Some(hashedPass: String) => password.isBcrypted(hashedPass)
      case None => throw UserNotExistsWithUsername(s"No user exists with userName: $userName")
    }
  }
}
