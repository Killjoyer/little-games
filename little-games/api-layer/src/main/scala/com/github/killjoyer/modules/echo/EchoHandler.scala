package com.github.killjoyer.modules.echo

import zio.UIO
import zio.ZIO
import zio.ZLayer
import zio.stream.Stream

final case class EchoHandler() {

  def echo(input: String): ZIO[Any, Nothing, String] = ZIO.succeed(input.reverse)

  val websocketEcho: UIO[Stream[Throwable, String] => Stream[Throwable, String]] =
    ZIO.succeed(_.map(_.reverse))

}

object EchoHandler {

  val layer: ZLayer[Any, Nothing, EchoHandler] = ZLayer.fromFunction(() => new EchoHandler())

}
