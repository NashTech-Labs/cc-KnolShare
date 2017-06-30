package service

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

import com.google.inject.Inject
import com.knoldus.dao.services.user.AdminDBService
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.models.{Admin, User}

class AdminService @Inject()(adminDBService: AdminDBService) {

  def validateUser(email: String): Future[Admin] = {
    Try(adminDBService.getAdminByEmail(email)) match {
      case Success(futureAdminOpt) => {
        futureAdminOpt.map {
          adminOpt =>
            adminOpt.fold {
              throw UserNotFoundException("User With This Email Does Not Exists")
            } (identity)
        }
      }
      case Failure(_) => throw UserNotFoundException("User With This Email Does Not Exists")
    }
  }

}
