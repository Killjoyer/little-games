package com.github.killjoyer.services.application.traits

import com.github.killjoyer.domain.lobby.Lobby.LobbyId
import com.github.killjoyer.domain.users.UserId
import zio.Task

trait LobbyManager {
  def createLobby(initiator: UserId): Task[Unit]

  def addPlayer(lobbyId: LobbyId, player: UserId): Task[Unit]
}
