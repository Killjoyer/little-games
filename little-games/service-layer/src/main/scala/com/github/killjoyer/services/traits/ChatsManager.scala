package com.github.killjoyer.services.traits
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.Username
import zio.Task

trait ChatsManager {
  def registerToChat(username: Username, chatId: ChatId): Task[Unit]

  def sendMessageToChat(username: Username, chatId: ChatId, message: String): Task[Unit]
}
