package com.knoldus.dao.connection

/**
 * Created by knoldus on 15/6/17.
 */
trait PostgresDBComponent extends DBComponent {

  val driver = PostgresDriver
  import driver.api._
  val db = PostgresDB.connectionPool
}

private[connection] object PostgresDB {
  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("postgresdbconf")
}