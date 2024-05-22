package com.github.killjoyer.modules.chat
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.services.traits.ChatsManager
import zio.UIO
import zio.ZLayer

case class SimpleChatHandler(chatsManager: ChatsManager) {
  def registerToChat(chatId: ChatId, username: Username): UIO[Unit] =
    chatsManager.registerToChat(username, chatId).ignore // todo catch errors

  def sendMessage(chatId: ChatId, username: Username, message: String): UIO[Unit] =
    chatsManager.sendMessageToChat(username, chatId, message).ignore // todo catch errors
}

object SimpleChatHandler {
  val layer: ZLayer[ChatsManager, Nothing, SimpleChatHandler] = ZLayer.fromFunction(SimpleChatHandler.apply _)
}
