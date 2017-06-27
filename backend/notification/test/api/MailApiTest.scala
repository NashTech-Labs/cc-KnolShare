package api

import org.scalatest.WordSpecLike

class MailApiTest extends WordSpecLike {

  private val sendTo = List("anubhavtarar40@gmail.com")
  private val recipentlist = List("anubhavtarar40@gmail.com", "anubhav.tarar@knoldus.in")

  object MailApiTestObj extends MailApi

  "MailApi" should {
    "be able to send mail for Single Recipent" in {
      val actual = MailApiTestObj.sendMail(sendTo, "hi", "hi")
      assert(actual)

    }
    "be able to send mail for Multiple Recipent" in {
      val recipentlist = List("anubhavtarar40@gmail.com", "anubhav.tarar@knoldus.in")
      val actual = MailApiTestObj.sendMail(sendTo, "hi", "hi")
      assert(actual)

    }
  }
}