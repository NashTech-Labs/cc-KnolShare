package com.knoldus.dao.components


import java.sql.Date

import com.knoldus.dao.connection.{DBComponent, PostgresDBComponent}
import com.knoldus.models.KnolxSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait KnolxSessionComponent extends KnolxSessionTable {
  this: DBComponent =>

  import driver.api._

  def createKnolxSession(knolx: KnolxSession): Future[KnolxSession] = {
    db.run {
      knolxSessionTableQuery returning knolxSessionTableQuery.map(_.id) += knolx
    }
      .map(knolxId => knolx.copy(id = knolxId))
  }

  def updateKnolxById(id: Int, knolx: KnolxSession): Future[Int] = {
    db.run {
      knolxSessionTableQuery.filter(_.id === id).update(knolx)
    }
  }

  def deleteKnolxById(id: Int): Future[Int] = {
    db.run {
      knolxSessionTableQuery.filter(_.id === id).delete
    }
  }

  def getKnolxByDate(date: Date): Future[Option[KnolxSession]] = {
    db.run(knolxSessionTableQuery.filter(_.date === date).result.headOption)
  }

  def getKnolxByPresentor(presentor: String): Future[Option[KnolxSession]] = {
    db.run(knolxSessionTableQuery.filter(_.presentor === presentor).result.headOption)
  }
}

object KnolxSessionComponent extends KnolxSessionComponent with PostgresDBComponent


