package com.killjoyer

import cats.syntax.all._
import com.killjoyer.infrastructure.config.ConfigLoader
import com.killjoyer.infrastructure.http.{RouteProvider, ZioHttpServer}
import com.killjoyer.modules.echo.{BullsAndCowsHandler, BullsAndCowsModule, EchoHandler, EchoModule}
import com.killjoyer.repositories.impls.StubDictionaryRepository
import com.killjoyer.services.impls.BullsAndCowsServiceLive
import tofu.logging.zlogs._
import zio._

object Main extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> TofuZLogger.addToRuntime

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[ZioHttpServer](_.start)
      .provide(
        ConfigLoader.load(),
        ZioHttpServer.layer,
        RouteProvider.layer,
        EchoModule.layer,
        EchoHandler.layer,
        BullsAndCowsServiceLive.layer,
        BullsAndCowsModule.layer,
        BullsAndCowsHandler.layer,
        ZLayer.succeed(Random.RandomLive),
        StubDictionaryRepository.layer
      )
}
