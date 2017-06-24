package com.knoldus.dao.services

import scala.concurrent.Future

import com.knoldus.dao.components.UserComponent
import com.knoldus.dao.services.user.UserDBService
import com.knoldus.models.User
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

class UserDBServiceSpec extends PlaySpecification with Mockito {

  val mockedUserComponent = mock[UserComponent]

  object TestDBService extends UserDBService {
    val userComponent: UserComponent = mockedUserComponent
  }

  sequential
  "User Service" should {

    "be able to get user by email" in {

      mockedUserComponent.getUserByEmail("sang@gmail.com") returns
      Future.successful(Some(User(1, "name", "email", "password", "123456")))
      val user = await(TestDBService.getUserByEmail("sang@gmail.com"))
      user.get.id must beEqualTo(1)
      user.get.email must beEqualTo("email")
    }

    "be able to check whether user exists or not" in {
      mockedUserComponent.getUserByEmailAndPassword("email", "password") returns
      Future.successful(Some(User(1, "name", "email", "password", "123456")))
      val res = await(TestDBService.isUserExists("email", "password"))
      res must beEqualTo(true)

    }

    "be able to create user" in {
      mockedUserComponent.createUser(User(1, "name", "email", "password", "123456")) returns
      Future.successful(User(1, "name", "email", "password", "123456"))
      val res = await(TestDBService.createUser(User(1, "name", "email", "password", "123456")))
      res.id must beEqualTo(1)
      res.email must beEqualTo("email")

    }
  }

}
