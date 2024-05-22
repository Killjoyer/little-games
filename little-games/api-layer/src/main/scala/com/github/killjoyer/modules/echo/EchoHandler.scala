package com.github.killjoyer.modules.echo

import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.services.traits.UserEventsRouter
import zio.Scope
import zio.UIO
import zio.ZIO
import zio.ZLayer
import zio.stream.Stream
import zio.stream.ZStream

final case class EchoHandler(usersRouter: UserEventsRouter) {

  val websocketEcho: UIO[Stream[Throwable, String] => Stream[Throwable, String]] =
    ZIO.succeed(_.map(_.reverse))

  val userSpecificEcho: ZIO[Any, Nothing, Stream[Throwable, String] => Stream[Throwable, String]] =
    ZIO.succeed { inputs =>
      ZStream
        .fromZIO(usersRouter.registerRoute(inputs))
        .flatten
        .provideLayer(Scope.default)
    }

  def echo(input: String): ZIO[Any, Nothing, String] = ZIO.succeed(input.reverse)

  def publish(receiver: Username, event: String): ZIO[Any, Nothing, String] =
    usersRouter.sendEvent(receiver, event).map(if (_) "sent" else "not sent")
}

object EchoHandler {

  val layer: ZLayer[UserEventsRouter, Nothing, EchoHandler] = ZLayer.fromFunction(EchoHandler.apply _)

}
