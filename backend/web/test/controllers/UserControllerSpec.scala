package controllers

import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, PlaySpecification, WithApplication}


class UserControllerSpec extends PlaySpecification{

  "respond to the index Action" in new WithApplication {
    val Some(result) = route(app, FakeRequest(GET, "/Bob"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("text/html")
    charset(result) must beSome("utf-8")
    contentAsString(result) must contain("Hello Bob")
  }
}
