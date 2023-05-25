package com.killjoyer

import cats.Order
import cats.syntax.all._
import com.killjoyer.infrastructure.config.ConfigLoader
import com.killjoyer.infrastructure.http.{RouteProvider, ZioHttpServer}
import com.killjoyer.modules.echo.{EchoHandler, EchoModule}
import shapeless.syntax.std.function.fnUnHListOps
import tofu.logging.zlogs._
import zio._
import zio.http.Server
import zio.stream.ZStream

object Main extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> TofuZLogger.addToRuntime

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[ZioHttpServer](_.start)
      .provide(ConfigLoader.load(), ZioHttpServer.layer, RouteProvider.layer, EchoModule.layer, EchoHandler.layer)
}
