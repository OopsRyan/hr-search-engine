
package auth

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.Future

case class UserAuth(email: String, passwordHash: String, creationTime: Long)

@Singleton
class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  implicit val getUserResult = GetResult(r => UserAuth(r.nextString(), r.nextString(), r.nextLong()))

  def getUserByEmail(email: String): Future[Option[UserAuth]] = {
    db.run(sql"select email, passwdHash, creationTime from users where email = ${email}".as[UserAuth].headOption)
  }

  def createUser(user: UserAuth): Future[Int] = {
    db.run(sqlu"insert into users (email, passwdHash, creationTime) VALUES " +
      sqlu"(${user.email}, ${user.passwordHash}, ${user.creationTime})")
  }

  def getAllUsers: Future[Vector[String]] = {
    db.run(sql"select email from users".as[String])
  }
}