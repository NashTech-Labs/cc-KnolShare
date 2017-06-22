package com.knoldus.dao.connection

import slick.driver.H2Driver


trait TestDBHelper extends DBComponent {

  val driver = H2Driver
  import driver.api._

  val SCHEMA = getClass.getResource("/schema.sql").getPath

  private val h2Url = s"jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;INIT=" +
                      s"runscript from '$SCHEMA'"

  val db: driver.backend.DatabaseDef = Database.forURL(url = h2Url, driver = "org.h2.Driver")

}
