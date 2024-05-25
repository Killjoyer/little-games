package com.github.killjoyer.modules.echo

import com.github.killjoyer.infrastructure.utils.newtype._
import com.github.killjoyer.services.traits.UserEventsRouter
import io.circe.Json
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.auto._
import io.circe.syntax.EncoderOps
import zio.Scope
import zio.ZIO
import zio.ZLayer
import zio.stream.Stream
import zio.stream.ZStream

final case class UsersHandler(usersRouter: UserEventsRouter) {
  import UsersHandler._

  val userSpecificEcho: ZIO[Any, Nothing, Stream[Throwable, String] => Stream[Throwable, Json]] =
    ZIO.succeed { inputs =>
      ZStream
        .fromZIO(usersRouter.registerRoute(inputs))
        .flatten
        .provideLayer(Scope.default)
        .map(v => v.asJson)
    }
}

object UsersHandler {

  implicit val configuration: Configuration =
    Configuration.default.withDiscriminator("type")

  val layer: ZLayer[UserEventsRouter, Nothing, UsersHandler] = ZLayer.fromFunction(UsersHandler.apply _)
}
