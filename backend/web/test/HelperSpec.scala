import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import userHelper.HelperImpl

class HelperSpec extends PlaySpecification with Mockito {

  val helper = new HelperImpl

  object TestObject extends HelperImpl

  sequential
  "HelperImpl" should {

    "validate password" in {
      helper.validatePassWord("password", "password") must beEqualTo(true)
    }
  }
}
