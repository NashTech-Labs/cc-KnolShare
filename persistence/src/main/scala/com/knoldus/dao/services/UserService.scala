package com.knoldus.dao.services

import com.google.inject.ImplementedBy
import com.knoldus.dao.components.UserComponent

/**
 * Created by knoldus on 17/6/17.
 */
@ImplementedBy(classOf[UserServiceImpl])
trait UserService {

  val userComponent: UserComponent

}

class UserServiceImpl extends UserService {
  val userComponent = UserComponent
}
