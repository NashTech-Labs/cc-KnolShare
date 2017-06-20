import api.MailApi
import org.specs2.mutable._
import play.api.test.WithApplication

class MailApiSpec extends Specification {

  val mailApi = new MailApi
  val sendTo = List("anubhavtarar40@gmail.com")
  "MailApi" should {
    "be able to send mail for Single Recipent" in new WithApplication {
      val actual = mailApi.sendMail(sendTo, "hi", "hi")
      actual must beTrue

    }
    "be able to send mail for Multiplt Recipent" in new WithApplication {
      val recipentlist = List("anubhavtarar40@gmail.com","anubhav.tarar@knoldus.in")
      val actual = mailApi.sendMail(sendTo, "hi", "hi")
      actual must beTrue

    }
  }
}