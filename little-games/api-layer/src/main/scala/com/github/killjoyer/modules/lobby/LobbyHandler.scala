package com.github.killjoyer.modules.lobby

import com.github.killjoyer.modules.lobby.LobbyModule.CreateLobbyRequest
import com.github.killjoyer.services.application.traits.LobbyManager
import zio._

final case class LobbyHandler(lobbyManager: LobbyManager) {
  def createLobby(createLobbyRequest: CreateLobbyRequest): UIO[Unit] = ???
}

object LobbyHandler {
  val layer: ZLayer[LobbyManager, Nothing, LobbyHandler] = ZLayer.fromFunction(LobbyHandler.apply _)
}