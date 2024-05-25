package com.github.killjoyer.infrastructure.http

import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule
import com.github.killjoyer.modules.chat.SimpleChatModule
import com.github.killjoyer.modules.echo.UsersModule
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.&
import zio.Task
import zio.ZLayer

final case class RouteProvider(
    echoModule: UsersModule,
    bullsAndCowsModule: BullsAndCowsModule,
    chatsModule: SimpleChatModule,
) {

  private val swaggerEndpoints =
    SwaggerInterpreter()
      .fromServerEndpoints[Task](
        echoModule.endpoints ++ bullsAndCowsModule.endpoints ++ chatsModule.endpoints,
        "Cartographers",
        "1.0",
      )

  val routes: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] =
    echoModule.endpoints ++ bullsAndCowsModule.endpoints ++ chatsModule.endpoints ++ swaggerEndpoints

}

object RouteProvider {

  val layer: ZLayer[UsersModule & BullsAndCowsModule & SimpleChatModule, Nothing, RouteProvider] =
    ZLayer.fromFunction(RouteProvider.apply _)

}
