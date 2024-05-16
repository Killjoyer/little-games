package com.github.killjoyer.infrastructure.http

import com.github.killjoyer.infrastructure.config.HttpServerConfig
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio._
import zio.http._

class ZioHttpServer(routeProvider: RouteProvider, config: HttpServerConfig) {

  def start: ZIO[Any, Throwable, Nothing] =
    Server
      .serve(ZioHttpInterpreter().toHttp(routeProvider.routes))
      .provide(ZLayer.succeed(Server.Config.default.binding(config.host, config.port)), Server.live)

}

object ZioHttpServer {

  val layer: ZLayer[RouteProvider with HttpServerConfig, Nothing, ZioHttpServer] =
    ZLayer.fromFunction(new ZioHttpServer(_, _))

}
