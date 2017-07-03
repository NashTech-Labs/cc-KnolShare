package com.knoldus.dao.services.user

import java.sql.Date

import com.knoldus.dao.components.KnolxSessionComponent
import com.knoldus.models.KnolxSession

import scala.concurrent.Future

trait KnolxSessionDBService {

  val knolxSessionComponent: KnolxSessionComponent

  def createKnolxSession(knolx: KnolxSession): Future[KnolxSession] = {
    knolxSessionComponent.createKnolxSession(knolx)
  }

  def getAllKnolxSessions(): Future[List[KnolxSession]] = {
    knolxSessionComponent.getAllKnolxSession()
  }

  def getKnolxByPresenter(presenter: String): Future[Option[KnolxSession]] = {
    knolxSessionComponent.getKnolxByPresentor(presenter)
  }

  def getKnolxByDate(date: Date): Future[Option[KnolxSession]] = {
    knolxSessionComponent.getKnolxByDate(date)
  }

  def updateKnolxById(id: Int, knolx: KnolxSession): Future[Int] = {
    knolxSessionComponent.updateKnolxById(id, knolx)
  }

  def deleteKnolxById(id: Int): Future[Int] = {
    knolxSessionComponent.deleteKnolxById(id)
  }

  def getKnolxByNameAndDate(name: String, date: Date): Future[Option[KnolxSession]] = {
    knolxSessionComponent.getKnolxByNameAndDate(name, date)
  }

}

class KnolxSessionDBImpl extends KnolxSessionDBService {
  val knolxSessionComponent = KnolxSessionComponent
}
