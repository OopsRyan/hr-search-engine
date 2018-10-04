package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class LoginController @Inject() (cc: ControllerComponents) extends AbstractController(cc){

  def index() = Action { implicit request: Request[AnyContent] =>
    request.session.get("User") match {
      case Some(user) if isValidUser(user) =>
        val view = views.html.login(user.toString)
        Ok(view)
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("You are currently not logged in. \n Please login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def login(name: String, password: String) = Action { implicit request: Request[AnyContent] =>

    if(check(name, password))
      Redirect("/auth/index").withSession(("User", name))
    else
      BadRequest("Invalid username or password")
  }

  def logout() = Action {implicit request =>
    request.session.get("User") match {
      case Some(user) if isValidUser(user) => Ok("Successfully logged out").withNewSession
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("Not logged in. Please login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def check(name: String, password: String) = {
    name == "admin" && password == "1234"
  }

  def isValidUser(name: String) = {
    name == "admin"
  }

}
