package com.github.killjoyer.domain

import io.estatico.newtype.macros.newtype
import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.domain.chats.Chat.ChatId

package object lobby {
  @newtype
  case class LobbyId(value: String)

  case class Lobby(id: LobbyId, players: Set[Username], lobbyChat: ChatId)
}
