package com.knoldus.dao.components

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.User

/**
 * Created by knoldus on 17/6/17.
 */
trait UserComponent extends UserTable {
  this: DBComponent =>

  def createUser(user: User) = {
    db.run { userTableQuery returning userTableQuery.map(_.id) += user }

  }


}

object UserComponent extends UserComponent with PostgresDBComponent
