package com.killjoyer.infrastructure.http

import com.killjoyer.modules.echo.EchoModule
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{Task, ZLayer}
import sttp.tapir.swagger.bundle.SwaggerInterpreter

class RouteProvider(echoModule: EchoModule) {

  private val swaggerEndpoints = SwaggerInterpreter().fromServerEndpoints[Task](echoModule.endpoints, "Cartographers", "1.0")

  val routes: List[ZServerEndpoint[Any, ZioStreams]] = echoModule.endpoints ++ swaggerEndpoints
}

object RouteProvider {
  val layer: ZLayer[EchoModule, Nothing, RouteProvider] = ZLayer.fromFunction(new RouteProvider(_))
}
