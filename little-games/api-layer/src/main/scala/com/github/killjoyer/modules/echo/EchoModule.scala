package com.github.killjoyer.modules.echo

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.CodecFormat
import sttp.tapir.ztapir._
import zio._

final case class EchoModule(handler: EchoHandler) {

  private val baseEndpoint =
    endpoint
      .tag("Echo")
      .in("api" / "echo")

  private val echo: ZServerEndpoint[Any, Any] =
    baseEndpoint.get
      .in("plain" / path[String]("input"))
      .out(stringBody)
      .zServerLogic(handler.echo)

  private val publish: ZServerEndpoint[Any, Any] =
    baseEndpoint.post
      .in("publish" / query[String]("receiver") / query[String]("event"))
      .out(stringBody)
      .zServerLogic { case (receiver, event) =>
        handler.publish(receiver, event)
      }

  private val webSocketEcho: ZServerEndpoint[Any, ZioStreams with WebSockets] =
    baseEndpoint
      .in("websocket")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](ZioStreams))
      .zServerLogic(_ => handler.websocketEcho)

  private val userSpecificEcho: ZServerEndpoint[Any, ZioStreams with WebSockets] =
    baseEndpoint
      .in("users" / "websocket")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](ZioStreams))
      .zServerLogic(_ => handler.userSpecificEcho)

  val endpoints: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] =
    List(echo, publish, webSocketEcho, userSpecificEcho)

  val nonRenderEndpoints: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] = List()

}

object EchoModule {

  val layer: ZLayer[EchoHandler, Nothing, EchoModule] = ZLayer.fromFunction(new EchoModule(_))

}
