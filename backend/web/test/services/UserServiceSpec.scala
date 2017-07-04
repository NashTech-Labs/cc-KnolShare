package services

import com.knoldus.dao.services.user.UserDBService
import com.knoldus.exceptions.NotificationException.MailerDaemonException
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.models.User
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import service.UserService

import scala.concurrent.Future

class UserServiceSpec extends PlaySpecification with Mockito {

  val mockedUserDbService = mock[UserDBService]
  val mockedMailService = mock[MailServiceImpl]

  object TestObject extends UserService(mockedUserDbService, mockedMailService)

  sequential
  "UserServiceSpec" should {

    "validate password" in {
      val result = TestObject.validatePassWord("password", "password")
      result must beEqualTo(true)
    }

    "create user: Success Case" in {
      val user: User = User(None, "userName", "email@abc.com", "password", "1234567890")
      mockedUserDbService.createUser(user) returns Future.successful(user.copy(id = Some(1)))
      val result = await(TestObject.createUser(user))
      result must beEqualTo(user.copy(id = Some(1)))
    }

    "create user: Failure Case" in {
      val user: User = User(None, "userName", "email@abc.com", "password", "1234567890")
      mockedUserDbService.createUser(user) returns Future.failed(new Exception("Table does not exist"))
      await(TestObject.createUser(user)) must throwA[Exception]
    }

    "send Mail : Success Case" in {
      mockedMailService.sendMail(List("email@abc.com"), "subject", "content") returns true
      TestObject.sendMail(List("email@abc.com"), "subject", "content") must beEqualTo(true)
    }

    "send Mail : Failure Case" in {
      mockedMailService.sendMail(List("email@abc.com"), "subject", "content") returns false
      TestObject.sendMail(List("email@abc.com"), "subject", "content") must throwA[MailerDaemonException]
    }

    "validate user : Success Case with Some User" in {
      val user: User = User(None, "userName", "email@abc.com", "password", "1234567890")
      mockedUserDbService.getUserByEmail("email@abc.com") returns Future.successful(Some(user))
      val result: User = await(TestObject.validateUser("email@abc.com"))
      result must beEqualTo(user)
    }

    "validate user : Success Case with None User" in {
      mockedUserDbService.getUserByEmail("email@abc.com") returns Future.successful(None)
      await(TestObject.validateUser("email@abc.com")) must throwA[UserNotFoundException]
    }


  }

}
