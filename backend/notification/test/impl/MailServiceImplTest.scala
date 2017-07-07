package impl

import api.MailApi
import org.scalatest.WordSpecLike
import services.MailServiceImpl


class MailServiceImplTest extends WordSpecLike {

  val mailServiceImpl = new MailServiceImpl

  "MailServiceImpl" should {
    "be able to give a MailApi Object back" in {
      val actual = mailServiceImpl.mailApi
      assert(actual.isInstanceOf[MailApi])
    }
  }
}
