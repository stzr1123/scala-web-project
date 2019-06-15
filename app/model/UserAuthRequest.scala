package model

import play.api.mvc.{Request, WrappedRequest}

/**
  * Request wrapper that adds the user, can be used with all usual HTTP content types.
  * @param user
  * @param request
  * @tparam A
  */
case class UserAuthRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)
