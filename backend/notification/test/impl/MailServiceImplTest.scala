package impl

import api.MailApi
import org.scalatest.WordSpecLike
import services.MailServiceImpl


class MailServiceImplTest extends WordSpecLike {
  "MailServiceImpl" should {
    "be able to give a MailApi Object back" in {
      val actual = MailServiceImpl.mailApi
      assert(actual.isInstanceOf[MailApi])
    }
  }
}
