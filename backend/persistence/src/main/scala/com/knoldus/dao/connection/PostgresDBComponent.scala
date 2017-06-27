package com.knoldus.dao.connection

import slick.driver.PostgresDriver

trait PostgresDBComponent extends DBComponent {

  val driver = PostgresDriver

  val db = PostgresDB.connectionPool
}

private[connection] object PostgresDB {

  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("postgresdbconf")
}
