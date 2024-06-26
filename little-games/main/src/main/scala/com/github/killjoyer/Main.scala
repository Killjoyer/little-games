package com.github.killjoyer

import cats.syntax.all._

import com.github.killjoyer.infrastructure.config.ConfigLoader
import com.github.killjoyer.infrastructure.database.TransactorProvider
import com.github.killjoyer.infrastructure.http.RouteProvider
import com.github.killjoyer.infrastructure.http.ZioHttpServer
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsHandler
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule
import com.github.killjoyer.modules.chat.SimpleChatHandler
import com.github.killjoyer.modules.chat.SimpleChatModule
import com.github.killjoyer.modules.echo.UsersHandler
import com.github.killjoyer.modules.echo.UsersModule
import com.github.killjoyer.repositories.impls.RuDbDictionaryRepository
import com.github.killjoyer.services.application.impls.ChatsManagerLive
import com.github.killjoyer.services.application.impls.UserEventsRouterLive
import com.github.killjoyer.services.domain.impls.BullsAndCowsServiceLive
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
        UsersModule.layer,
        UsersHandler.layer,
        BullsAndCowsServiceLive.layer,
        BullsAndCowsModule.layer,
        BullsAndCowsHandler.layer,
        ZLayer.succeed(Random.RandomLive),
        RuDbDictionaryRepository.layer,
        TransactorProvider.transactorLayer,
        UserEventsRouterLive.layer,
        SimpleChatModule.layer,
        SimpleChatHandler.layer,
        ChatsManagerLive.layer,
      )
}
