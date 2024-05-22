package com.github.killjoyer.modules.echo

import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.modules.AppEndpoint
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.CodecFormat
import sttp.tapir.codec.newtype.codecForNewType
import sttp.tapir.ztapir._
import zio._

final case class EchoModule(handler: EchoHandler) {

  private val baseEndpoint =
    endpoint
      .tag("Echo")
      .in("api" / "echo")

  private val echo: AppEndpoint =
    baseEndpoint.get
      .in("plain" / path[String]("input"))
      .out(stringBody)
      .zServerLogic(handler.echo)

  private val publish: AppEndpoint =
    baseEndpoint.post
      .in("publish" / query[Username]("receiver") / query[String]("event"))
      .out(stringBody)
      .zServerLogic { case (receiver, event) =>
        handler.publish(receiver, event)
      }

  private val webSocketEcho: AppEndpoint =
    baseEndpoint
      .in("websocket")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](ZioStreams))
      .zServerLogic(_ => handler.websocketEcho)

  private val userSpecificEcho: AppEndpoint =
    baseEndpoint
      .in("users" / "websocket")
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](ZioStreams))
      .zServerLogic(_ => handler.userSpecificEcho)

  val endpoints: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] =
    List(echo, publish, webSocketEcho, userSpecificEcho)
}

object EchoModule {

  val layer: ZLayer[EchoHandler, Nothing, EchoModule] = ZLayer.fromFunction(new EchoModule(_))

}
