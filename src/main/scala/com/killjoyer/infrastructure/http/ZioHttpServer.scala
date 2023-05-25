package com.killjoyer.infrastructure.http

import com.killjoyer.infrastructure.config.HttpServerConfig
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio._
import zio.http._

class ZioHttpServer(routeProvider: RouteProvider, config: HttpServerConfig) {

  def start: ZIO[Any, Throwable, Nothing] =
    Server
      .serve(ZioHttpInterpreter().toHttp(routeProvider.routes).withDefaultErrorResponse)
      .provide(ZLayer.succeed(Server.Config.default.binding(config.host, config.port)), Server.live)
}

object ZioHttpServer {
  val layer = ZLayer.fromFunction(new ZioHttpServer(_, _))
}
