package util.exception

class KnolShareException(msg: scala.Predef.String) extends scala.IllegalArgumentException{
  override def getMessage: String = msg
}

case class UserNotExistsWithUsername(msg: scala.Predef.String) extends KnolShareException(msg)
