package com.knoldus.dao.services

import com.google.inject.ImplementedBy
import com.knoldus.dao.components.UserComponent

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {
  val userComponent: UserComponent
}

class UserServiceImpl extends UserService {
  val userComponent = UserComponent
}
