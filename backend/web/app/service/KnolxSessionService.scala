package service

import com.google.inject.Inject
import com.knoldus.dao.services.user.KnolxSessionDBService
import com.knoldus.exceptions.PSqlException.InsertionError
import com.knoldus.models.KnolxSession

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class KnolxSessionService @Inject()(val knolxSessionDBService: KnolxSessionDBService) {

  def createKnolxSession(knolx: KnolxSession): Future[KnolxSession] = {
    Try(knolxSessionDBService.createKnolxSession(knolx)) match {
      case Success(knolx) => knolx
      case Failure(_) => throw InsertionError("unable to create new knolx session")
    }
  }
}
