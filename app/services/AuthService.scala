package services

import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import java.util.{Base64, UUID}

import play.api.mvc.Cookie
import play.api.cache.SyncCacheApi
import model.User
import org.mindrot.jbcrypt.BCrypt
import scalikejdbc._

import scala.concurrent.duration.Duration

class AuthService(cacheApi: SyncCacheApi) {
  val mda: MessageDigest = MessageDigest.getInstance("SHA-512")
  val cookieHeader = "X-Auth-Token"

  def login(userCode: String, password: String): Option[Cookie] = {
    for {
      user <- checkUser(userCode, password)
      cookie <- Some(createCookie(user))
    } yield {
      cookie
    }
  }

  private def checkUser(userCode: String, password: String): Option[User] = {
    DB.readOnly { implicit session =>
      // the apply method below takes the implicit session parameter
      val maybeUser: Option[User] = sql"select * from users where user_code = $userCode".map(User.fromRS).single.apply
      maybeUser.flatMap { user =>
        if (BCrypt.checkpw(password, user.password)) {
          Some(user)
        } else None
      }
    }
  }

  private def createCookie(user: User): Cookie = {
    val randomPart = UUID.randomUUID().toString.toUpperCase
    val userPart = user.userId.toString.toUpperCase
    val key = s"$randomPart|$userPart"
    val token = Base64.getEncoder.encodeToString(mda.digest(key.getBytes))
    val duration = Duration.create(10, TimeUnit.HOURS)
    cacheApi.set(token, user, duration)
    Cookie(cookieHeader, token, maxAge = Some(duration.toSeconds.toInt))
  }
}
