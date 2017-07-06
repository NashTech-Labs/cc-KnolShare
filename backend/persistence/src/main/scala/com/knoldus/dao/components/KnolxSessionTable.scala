package com.knoldus.dao.components


import java.sql.Date

import com.knoldus.dao.connection.DBComponent
import com.knoldus.models.KnolxSession
import slick.lifted.ProvenShape

trait KnolxSessionTable {

  this: DBComponent =>

  import driver.api._

  private[components] class KnolxSessionTable(tag: Tag) extends Table[KnolxSession](tag, "knolxsession") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val presentor = column[String]("presentor")
    val topic = column[Option[String]]("topic")
    val sessionId: Column[Option[Int]] = column[Option[Int]]("session_id")
    val rating = column[Option[Int]]("rating")
    val date = column[Date]("scheduled_date")

    def * : ProvenShape[KnolxSession] = (id, presentor, topic, sessionId, rating, date) <> ((KnolxSession.apply _).tupled, KnolxSession.unapply)
  }

  lazy val knolxSessionTableQuery = TableQuery[KnolxSessionTable]
}
