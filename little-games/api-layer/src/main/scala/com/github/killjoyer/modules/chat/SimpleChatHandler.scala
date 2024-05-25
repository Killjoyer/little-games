package com.github.killjoyer.modules.chat
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.infrastructure.utils.newtype._
import com.github.killjoyer.modules.chat.SimpleChatHandler.ChatMessage
import com.github.killjoyer.services.traits.ChatsManager
import io.circe.generic.JsonCodec
import zio.UIO
import zio.ZLayer

case class SimpleChatHandler(chatsManager: ChatsManager) {
  def registerToChat(chatId: ChatId, username: Username): UIO[Unit] =
    chatsManager.registerToChat(username, chatId).ignore // todo catch errors

  def sendMessage(chatMessage: ChatMessage): UIO[Unit] =
    chatsManager.sendMessageToChat(chatMessage.username, chatMessage.chatId, chatMessage.message).ignore // todo catch errors
}

object SimpleChatHandler {
  @JsonCodec
  case class ChatMessage(message: String, username: Username, chatId: ChatId)

  val layer: ZLayer[ChatsManager, Nothing, SimpleChatHandler] = ZLayer.fromFunction(SimpleChatHandler.apply _)
}
