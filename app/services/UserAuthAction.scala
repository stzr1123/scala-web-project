package services

import model.UserAuthRequest
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, PlayBodyParsers, Request, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

class UserAuthAction(authService: AuthService, ec: ExecutionContext, playBodyParsers: PlayBodyParsers)
  extends ActionBuilder[UserAuthRequest, AnyContent]{

  override val executionContext: ExecutionContext = ec
  override def parser: BodyParser[AnyContent] = playBodyParsers.defaultBodyParser

  def invokeBlock[A](request: Request[A], block: UserAuthRequest[A] => Future[Result]): Future[Result] = {
    val maybeUser = authService.checkCookie(request)
    maybeUser match {
      case None => Future.successful(Results.Redirect("/login"))
      case Some(user) => block(UserAuthRequest(user, request))
    }
  }

}
