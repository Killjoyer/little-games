package com.github.killjoyer.domain

import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.Username
import io.estatico.newtype.macros.newtype
import zio.Hub

package object chats {

  case class Chat(chatId: ChatId, events: Hub[String], members: Set[Username])

  object Chat {
    @newtype case class ChatId(value: String)
  }
}
