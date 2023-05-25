package com.killjoyer.modules.echo

import zio.{ZIO, ZLayer}

case class EchoHandler() {
  def echo(input: String): ZIO[Any, Nothing, String] = ZIO.succeed(input.reverse)
}

object EchoHandler {
  val layer: ZLayer[Any, Nothing, EchoHandler] = ZLayer.fromFunction(() => new EchoHandler())
}
