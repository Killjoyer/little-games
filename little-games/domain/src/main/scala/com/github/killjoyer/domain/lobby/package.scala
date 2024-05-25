package com.github.killjoyer.domain

import io.estatico.newtype.macros.newtype
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.lobby.Lobby.LobbyId
import com.github.killjoyer.domain.users.UserId

package object lobby {

  case class Lobby(id: LobbyId, players: Set[UserId], lobbyChat: ChatId, host: UserId)

  object Lobby {

    @newtype
    case class LobbyId(value: String)
  }
}
