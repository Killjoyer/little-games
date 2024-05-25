package com.github.killjoyer.services.application.traits

import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.UserId
import zio.Task

trait ChatsManager {
  def registerToChat(username: UserId, chatId: ChatId): Task[Unit]

  def sendMessageToChat(username: UserId, chatId: ChatId, message: String): Task[Unit]
}
