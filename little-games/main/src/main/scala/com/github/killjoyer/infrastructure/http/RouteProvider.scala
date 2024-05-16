package com.github.killjoyer.infrastructure.http

import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule
import com.github.killjoyer.modules.echo.EchoModule
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.&
import zio.Task
import zio.ZLayer

final case class RouteProvider(echoModule: EchoModule, bullsAndCowsModule: BullsAndCowsModule) {

  private val swaggerEndpoints =
    SwaggerInterpreter()
      .fromServerEndpoints[Task](echoModule.endpoints ++ bullsAndCowsModule.endpoints, "Cartographers", "1.0")

  val routes: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] =
    echoModule.endpoints ++ bullsAndCowsModule.endpoints ++ swaggerEndpoints ++ echoModule.nonRenderEndpoints

}

object RouteProvider {

  val layer: ZLayer[EchoModule & BullsAndCowsModule, Nothing, RouteProvider] =
    ZLayer.fromFunction(RouteProvider.apply _)

}
