package controllers

import javax.inject._
import play.api._
import play.api.mvc._


@Singleton
class Users @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def addUser() = Action { implicit request: Request[AnyContent] =>
    val body = request.body

    body.asFormUrlEncoded match {
      case Some(map) =>
        Ok(s"The user of name `${map.get("name").head}` and age `${map("age").head}` has been created\n")
      case None => BadRequest("Unknown body format")
    }
  }

}
