package com.killjoyer.modules.echo

import sttp.tapir.ztapir._
import zio._

case class EchoModule(handler: EchoHandler) {

  private val echoEndpoint: ZServerEndpoint[Any, Any] =
    endpoint
      .tag("Echo")
      .get
      .in("api" / "echo" / path[String]("input"))
      .out(stringBody)
      .zServerLogic(handler.echo)

  val endpoints: List[ZServerEndpoint[Any, Any]] = List(echoEndpoint)
}

object EchoModule {
  val layer: ZLayer[EchoHandler, Nothing, EchoModule] = ZLayer.fromFunction(new EchoModule(_))
}
