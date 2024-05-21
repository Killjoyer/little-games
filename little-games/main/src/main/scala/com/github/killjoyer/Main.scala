package com.github.killjoyer

import cats.syntax.all._

import com.github.killjoyer.infrastructure.config.ConfigLoader
import com.github.killjoyer.infrastructure.database.TransactorProvider
import com.github.killjoyer.infrastructure.http.RouteProvider
import com.github.killjoyer.infrastructure.http.ZioHttpServer
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsHandler
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule
import com.github.killjoyer.modules.echo.EchoHandler
import com.github.killjoyer.modules.echo.EchoModule
import com.github.killjoyer.repositories.impls.RuDbDictionaryRepository
import com.github.killjoyer.services.impls.BullsAndCowsServiceLive
import com.github.killjoyer.services.impls.UserEventsRouterLive
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
        RuDbDictionaryRepository.layer,
        TransactorProvider.transactorLayer,
        UserEventsRouterLive.layer
      )

}
