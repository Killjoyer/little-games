package com.github.killjoyer.modules.lobby

import com.github.killjoyer.modules.AppEndpoint
import com.github.killjoyer.modules.lobby.LobbyModule.CreateLobbyRequest
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.ztapir._
import zio.ZLayer

final case class LobbyModule(handler: LobbyHandler) {
  private val baseEndpoint =
    endpoint
      .tag("lobby")
      .in("api" / "lobby")

  private val createLobby: AppEndpoint =
    baseEndpoint.post
      .in(jsonBody[CreateLobbyRequest])
      .out(emptyOutput)
      .zServerLogic(handler.createLobby)


  val endpoints: List[ZServerEndpoint[Any, AppEndpoint]] = ???
}

object LobbyModule {
  @JsonCodec
  final case class CreateLobbyRequest()

  val layer: ZLayer[LobbyHandler, Nothing, LobbyModule] = ZLayer.fromFunction(LobbyModule.apply _)
}