package controllers

import javax.inject._
import actors.{User, UserActor, ListActor}
import play.api._
import play.api.mvc._
import play.api.libs.json._

import akka.actor.ActorSystem
import akka.pattern.{ask, AskTimeoutException}
import akka.util.Timeout
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
object HomeController {
  val UNKNOWN_ACTOR_ERROR = "Hello \u266A Is it list(*user) you're looking for..."
  val TIMEOUT_ERROR = "Oops! It looks like the actors are taking their time :("
  val TIMEOUT = 5.seconds
}
@Singleton
class HomeController @Inject() (system: ActorSystem) extends Controller {
  import HomeController._

  // timeout for the ask pattern
  implicit val timeout = Timeout(TIMEOUT)

  // defs returning actor references
  def listActor = system.actorOf(ListActor.props)
  def userActor = system.actorOf(UserActor.props)

  def index = Action {
    Ok(views.html.index("This is not the droid you're looking for!"))
  }

  /**
   * entry point for the task
   * @param actor actor's name to call
   * @param param optional parameter for the actor
   * @return
   */
  def task(actor: String, param: Option[String] = None) = Action.async {
    actor match {
      case "list" => handleList(param)
      case "user" => handleUser(param)
      case _ => Future.successful(BadRequest(UNKNOWN_ACTOR_ERROR))
    }
  }

  /**
   * list task handler
   * @param param optional parameter for the list actor
   * @return
   */
  def handleList(param: Option[String]): Future[Result] = {
    (listActor ? param.getOrElse(None))
      .mapTo[Either[Int, List[Int]]]
      .map {
        res => res match {
          case Left(n) => Ok(Json.toJson(n))
          case Right(l) => Ok(Json.toJson(l))
        }
      }
      .recover(handleRecovery)
  }

  /**
   * user task handler
   * @param param optional parameter for the user actor
   * @return
   */
  def handleUser(param: Option[String]): Future[Result] = {
    (userActor ? param.getOrElse(None))
      .mapTo[List[User]]
      .map {
        res => Ok(Json.toJson(res))
      }
      .recover(handleRecovery)
  }

  /**
   * partial function used for the recovery if an actor times out
   */
  def handleRecovery: PartialFunction[Throwable, Result] = {
    case _: AskTimeoutException => InternalServerError(TIMEOUT_ERROR)
  }
}
