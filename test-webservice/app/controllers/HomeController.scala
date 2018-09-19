package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def hello(name: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Hello " + name)
  }

  def sqrt(num: String) = Action { implicit request: Request[AnyContent] =>
    Try(num.toInt) match {
      case Success(ans) if ans >= 0 => Ok(s"The answer is: ${math.sqrt(ans)}")
      case Success(ans) => Ok(s"The input ($num) must be greater than zero")
      case Failure(ex) => InternalServerError(s"Could not extract the contents from $num")
    }
  }
}
