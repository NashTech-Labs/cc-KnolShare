package service

import com.google.inject.Inject
import com.knoldus.dao.services.user.AdminDBService
import com.knoldus.exceptions.PSqlException.UserNotFoundException
import com.knoldus.models.Admin
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

class AdminService @Inject()(adminDBService: AdminDBService) {

  def validateUser(email: String): Future[Admin] = {
    adminDBService.getAdminByEmail(email) map { adminOpt =>
      adminOpt.fold {
        throw UserNotFoundException("User With This Email Does Not Exists")
      }(identity)
    }
  }

}
