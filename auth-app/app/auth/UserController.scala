package auth

import javax.inject.{Inject, Singleton}
import com.ryan.auth._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.Future


@Singleton
class UserController @Inject (userService: UserService, contexts: Contexts, tokenService: TokenService,
  cc: ControllerComponents) extends AbstractController(cc) {

  implicit val executionContext = contexts.cpuLookup

  def register = Action.async(parse.json) { implicit request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest("Not a valid input format: " + error.mkString)),
      user =>
        userService.userExists(user.email).flatMap(ifExits => {
          if (ifExits)
            Future.successful(BadRequest(s"User already exists: ${user.email}. Cannot register again"))
          else {
            userService.addUser(user)
              .flatMap(_ => tokenService.createToken(user.email))
              .map(x => Ok(Json.toJson(x.token)))
          }
        })
    )
  }

  def login = Action.async(parse.json) {implicit request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest("Not a valid input format: " + error.mkString)),
      user =>
        userService.validateUser(user.email, user.password).flatMap { validated =>
          if (validated)
            tokenService.createToken(user.email).map(x => Ok(Json.toJson(x.token)))
          else
            Future.successful(BadRequest("username/password mismatch"))
        }
    )
  }

  def logout(token: String) = Action.async{ implicit request =>
    val future = tokenService.authenticateToken(TokenStr(token), refresh = false)
    future.map(x => {
      tokenService.dropToken(x.token)
      Ok("logged out")
    }).recoverWith {
      case e: Exception => Future.successful(BadRequest(e.getMessage))
    }
  }

  def getAll = Action.async {
    userService.getAllUserNames.map(x => Ok(Json.toJson(x)))
  }
}