package controllers

import javax.inject._
import actors.User
import play.api._
import play.api.mvc._
import play.api.libs.json._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("This is not the droid your looking for!"))
  }

  /**
   * entry point for the task
   * @param actor actor's name to call
   * @param param optional parameter for the actor
   * @return
   */
  def task(actor: String, param: Option[String] = None) = Action {
    // call the actor according to the actor parameter
    // e.g. if actor is "List", call the ListActor actor
    // if the actor does not exist, return a BadRequest with an error message
    // if param is set, pass the param to the actor

    // write the result of the actor as response if successful, otherwise an error message

    // write User or list of User to json: Json.toJson(User("David", "Test", "test"))
    // or Json.arr(Json.toJson(User("David", "Test", "test")))

    Ok("Get to the chopper!")
  }

}
