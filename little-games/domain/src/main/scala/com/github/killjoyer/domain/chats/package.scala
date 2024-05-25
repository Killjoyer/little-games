package com.github.killjoyer.domain

import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.events.LittleGamesEvent.ChatEvent
import com.github.killjoyer.domain.users.UserId
import io.estatico.newtype.macros.newtype
import zio.Hub

package object chats {

  case class Chat(chatId: ChatId, events: Hub[ChatEvent], members: Set[UserId])

  object Chat {
    @newtype case class ChatId(value: String)
  }
}
