package actors

import akka.actor.{PoisonPill, Actor}
import play.api.libs.json._

case class User(name: String, country: String, city: String)
object User {
    implicit val userFormat: Format[User] = Json.format[User]
}

class UserActor extends Actor {

    lazy val list: List[User] = List(
        User("David", "Austria", "Graz"),
        User("Bernhard", "Austria", "Graz"),
        User("Rene", "Austria", "Eisenstadt"),
        User("Kristy", "New Zealand", "Auckland")
    )

    // sends back an List[User] to the sender
    def receive: Receive = {
        case "countryCode" =>
            // replace the country's name with the country code
            // e.g. Austria => AT
            sender() ! Left(0)
            self ! PoisonPill
        case x: String if x.startsWith("filter") =>
            // filter the list with filter options
            // e.g.
            // filter[country]:Austria should only return users from country Austria
            // filter[city]:Graz should only return users from city Graz
            sender() ! Left(0)
            self ! PoisonPill
        case _ =>
            sender() ! Right(getList)
            self ! PoisonPill
    }

    def getList: List[User] = {
        list
    }
}
