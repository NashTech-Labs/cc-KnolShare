package services

import api.{MailApi, SlackApi}
import org.mockito.Mock
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}


class MailServiceTest extends WordSpecLike with MustMatchers with MockitoSugar {

  private val sendTo = List("anubhavtarar40@gmail.com")
  private val invalidEmailId = List("invalidid@gmail.com")
  private val mockedMailApi = mock[MailApi]

  object MailServiceTestObj extends MailService {
    val mailApi: MailApi = mockedMailApi
    val passWord: String = "fakepassword"
    val userEmail: String = "fakeid@gmail.com"
  }

  "Mail Service" should {
    "be able to send the mail with valid credentials" in {
      when(MailServiceTestObj.sendMail(sendTo, "Testing", "HI")).thenReturn(true)
      assert(MailServiceTestObj.sendMail(sendTo, "Testing", "HI"))
    }
    "not be able to send the mail with Invalid credentials" in {
      when(MailServiceTestObj.sendMail(invalidEmailId
        , "Testing", "HI")).thenReturn(false)
      assert(!MailServiceTestObj.sendMail(invalidEmailId, "Testing", "HI"))
    }
  }
}
