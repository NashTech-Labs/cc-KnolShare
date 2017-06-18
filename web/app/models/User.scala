package models

/**
  * Created by knoldus on 18/6/17.
  */
case class User(firstName: String,
                 middleName: Option[String],  //Optional field
                 lastName: String,
                 userName: String,
                 password: String,            //encrypted
                 email: String,
                 slackId: Option[String],     //Optional field (for notification)
                 phone: Option[String]        //Optional field (for notification)
               )