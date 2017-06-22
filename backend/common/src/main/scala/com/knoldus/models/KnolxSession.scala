package com.knoldus.models

import java.sql.Date


case class KnolxSession(id: Int, presentor: String, topic: Option[String], session_id: Option[Int], rating: Option[Int], scheduledDate: Date)
