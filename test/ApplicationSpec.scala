import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import actors.User

class ApplicationSpec extends PlaySpec with OneAppPerTest {
  "Routes" should {
    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }
  }

  "HomeController" should {
    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("This is not the droid you&#x27;re looking for!")
    }

    "respond with a list of ints for /task/list" in {
      val list = route(app, FakeRequest(GET, "/task/list")).get
      val res = List(3,1,4,1,5,9,2,6,5,8,9,7,9,3,2,3,8)

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }

    "respond with a list of users for /task/user" in {
      val user = route(app, FakeRequest(GET, "/task/user")).get
      val res = List(User("David", "Austria", "Graz"),
        User("Bernhard", "Austria", "Graz"),
        User("Rene", "Austria", "Eisenstadt"),
        User("Kristy", "New Zealand", "Auckland"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with bad request message for unknown route eg. /task/unknown" in {
        val unknown = route(app, FakeRequest(GET, "/task/unknown")).get

        status(unknown) mustBe BAD_REQUEST
    }
  }

  "ListActor" should {
    "respond with sum of the list when called with 'sum'" in {
      val list = route(app, FakeRequest(GET, "/task/list?param=sum")).get
      val res = 85

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }

    "respond with average of the list when called with 'average'" in {
      val list = route(app, FakeRequest(GET, "/task/list?param=average")).get
      val res = 5

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }

    "respond with median of the list when called with 'median'" in {
      val list = route(app, FakeRequest(GET, "/task/list?param=median")).get
      val res = 5

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }

    "respond with the list when called with any other argument" in {
      val list = route(app, FakeRequest(GET, "/task/list?param=unknown")).get
      val res = List(3,1,4,1,5,9,2,6,5,8,9,7,9,3,2,3,8)

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }

    "respond with the list when called with no arguments" in {
      val list = route(app, FakeRequest(GET, "/task/list")).get
      val res = List(3,1,4,1,5,9,2,6,5,8,9,7,9,3,2,3,8)

      status(list) mustBe OK
      contentType(list) mustBe Some("application/json")
      contentAsJson(list) mustBe Json.toJson(res)
    }
  }

  "UserActor" should {
    "respond with the list with country codes for countries when called with 'countryCode'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=countryCode")).get
      val res = List(User("David", "AT", "Graz"),
        User("Bernhard", "AT", "Graz"),
        User("Rene", "AT", "Eisenstadt"),
        User("Kristy", "NZ", "Auckland"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with the filtered list when called with 'filter[name]:David'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=filter[name]:David")).get
      val res = List(User("David", "Austria", "Graz"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with the filtered list when called with 'filter[country]:New Zealand'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=filter[country]:New Zealand")).get
      val res = List(User("Kristy", "New Zealand", "Auckland"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with the filtered list when called with 'filter[city]:Graz'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=filter[city]:Graz")).get
      val res = List(User("David", "Austria", "Graz"),
        User("Bernhard", "Austria", "Graz"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with an empty list when called with 'filter[name]:Piotr'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=filter[name]:Piotr")).get
      val res = List[User]()

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with an empty list when called with 'filter[age]:24'" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=filter[age]:24")).get
      val res = List[User]()

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with the full list when called with any other argument" in {
      val user = route(app, FakeRequest(GET, "/task/user?param=unknown")).get
      val res = List(User("David", "Austria", "Graz"),
        User("Bernhard", "Austria", "Graz"),
        User("Rene", "Austria", "Eisenstadt"),
        User("Kristy", "New Zealand", "Auckland"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }

    "respond with the full list when called with no arguments" in {
      val user = route(app, FakeRequest(GET, "/task/user")).get
      val res = List(User("David", "Austria", "Graz"),
        User("Bernhard", "Austria", "Graz"),
        User("Rene", "Austria", "Eisenstadt"),
        User("Kristy", "New Zealand", "Auckland"))

      status(user) mustBe OK
      contentType(user) mustBe Some("application/json")
      contentAsJson(user) mustBe Json.toJson(res)
    }
  }
}
