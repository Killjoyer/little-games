package com.killjoyer.infrastructure.http

import com.killjoyer.modules.echo.{BullsAndCowsModule, EchoModule}
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{&, Task, ZLayer}
import sttp.tapir.swagger.bundle.SwaggerInterpreter

case class RouteProvider(echoModule: EchoModule, bullsAndCowsModule: BullsAndCowsModule) {

  private val swaggerEndpoints =
    SwaggerInterpreter().fromServerEndpoints[Task](echoModule.endpoints ++ bullsAndCowsModule.endpoints, "Cartographers", "1.0")

  val routes: List[ZServerEndpoint[Any, ZioStreams]] = echoModule.endpoints ++ bullsAndCowsModule.endpoints ++ swaggerEndpoints
}

object RouteProvider {
  val layer: ZLayer[EchoModule & BullsAndCowsModule, Nothing, RouteProvider] = ZLayer.fromFunction(RouteProvider.apply _)
}
