package com.github.killjoyer.modules.echo

import zio.ZIO
import zio.ZLayer

final case class EchoHandler() {
  def echo(input: String): ZIO[Any, Nothing, String] = ZIO.succeed(input.reverse)
}

object EchoHandler {
  val layer: ZLayer[Any, Nothing, EchoHandler] = ZLayer.fromFunction(() => new EchoHandler())
}
