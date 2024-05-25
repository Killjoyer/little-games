package com.github.killjoyer.domain.events

import com.github.killjoyer.domain.chats.Chat.ChatId

sealed trait LittleGamesEvent

object LittleGamesEvent {
  case class GameEvent(payload: GamePayload) extends LittleGamesEvent

  case class ChatEvent(chatId: ChatId, payload: ChatPayload) extends LittleGamesEvent
}
