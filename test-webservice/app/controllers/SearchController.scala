package controllers

import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class SearchController @Inject() (cc: ControllerComponents, ws: WSClient)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def get(name: String) = Action.async { implicit request =>
    ws.url("http://httpbin.org/get")
      .withHeaders("Content-Type" -> "application/json")
      .withQueryString("username" -> name)
      .get()
      .map { response => Ok(response.body) }
  }
}
