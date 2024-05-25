package com.github.killjoyer.modules.echo

import com.github.killjoyer.modules.AppEndpoint
import io.circe.Json
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.CodecFormat
import sttp.tapir.codec.newtype.codecForNewType
import sttp.tapir.json.circe._
import sttp.tapir.ztapir._
import zio._

final case class UsersModule(handler: UsersHandler) {

  private val baseEndpoint =
    endpoint
      .tag("Echo")
      .in("api" / "echo")

  private val userSpecificEcho: AppEndpoint =
    baseEndpoint
      .in("users" / "websocket")
      .out(webSocketBody[String, CodecFormat.TextPlain, Json, CodecFormat.Json](ZioStreams))
      .zServerLogic(_ => handler.userSpecificEcho)

  val endpoints: List[ZServerEndpoint[Any, ZioStreams with WebSockets]] =
    List(userSpecificEcho)
}

object UsersModule {

  val layer: ZLayer[UsersHandler, Nothing, UsersModule] = ZLayer.fromFunction(new UsersModule(_))

}
