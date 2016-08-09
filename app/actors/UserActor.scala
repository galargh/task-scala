package actors

import akka.actor.{PoisonPill, Actor, Props}
import play.api.libs.json._

import helpers.CountryCodes.CountryCodes

case class User(name: String, country: String, city: String) {
  // easy way to convert a case class to a map
  def toMap: Map[String, Any] = this.getClass.getDeclaredFields.map(_.getName).zip(this.productIterator.to).toMap
}
object User {
  implicit val userFormat: Format[User] = Json.format[User]
}

object UserActor {
  def props = Props[UserActor]
}
class UserActor extends Actor {
  import UserActor._

  lazy val list: List[User] = List(
    User("David", "Austria", "Graz"),
    User("Bernhard", "Austria", "Graz"),
    User("Rene", "Austria", "Eisenstadt"),
    User("Kristy", "New Zealand", "Auckland")
  )

  // regex extractor for the filter param
  // extracts 2 groups:
  //  -filter name
  //  -filter value
  val Filter = "^filter\\[([^\\]]+)\\]:(.+)$".r

  // sends back an List[User] to the sender
  def receive: Receive = {
    case "countryCode" =>
      sender() ! getListWithCountryCodes
      self ! PoisonPill
    case Filter(key, value) =>
      sender() ! getFilteredList(key, value)
      self ! PoisonPill
    case _ =>
      sender() ! getList
      self ! PoisonPill
  }

  def getList: List[User] = {
    list
  }

  def getListWithCountryCodes: List[User] = {
    getList map {
      user => User(user.name, CountryCodes.getOrElse(user.country, "UNKNOWN"), user.city)
    }
  }

  def getFilteredList(key: String, value: String): List[User] = {
    // single match on the key
    val f = key match {
      case "name" => ((u: User) => u.name == value)
      case "country" => ((u: User) => u.country == value)
      case "city" => ((u: User) => u.city == value)
      case _ => ((u: User) => false)
    }
    getList filter f
  }

  def getFilteredListForAnyFilter(key: String, value: String): List[User] = {
    getList filter (user => user.toMap(key) == value)
  }
}
