package com.github.killjoyer.modules.chat

import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.UserId
import com.github.killjoyer.modules.AppEndpoint
import com.github.killjoyer.modules.chat.SimpleChatHandler.ChatMessage
import io.circe.generic.auto._
import sttp.tapir.codec.newtype._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.ztapir._
import zio.ZLayer

case class SimpleChatModule(handler: SimpleChatHandler) {
  private val baseEndpoint =
    endpoint
      .tag("Chat")
      .in("api" / "chat")

  private val singInToChat: AppEndpoint =
    baseEndpoint.post
      .in("sign-in" / query[ChatId]("chatId") / query[UserId]("username"))
      .zServerLogic((handler.registerToChat _).tupled)

  private val sendMessage: AppEndpoint =
    baseEndpoint.post
      .in("send")
      .in(jsonBody[ChatMessage])
      .zServerLogic(handler.sendMessage(_))

  val endpoints: List[AppEndpoint] = List(singInToChat, sendMessage)
}

object SimpleChatModule {
  val layer: ZLayer[SimpleChatHandler, Nothing, SimpleChatModule] = ZLayer.fromFunction(SimpleChatModule.apply _)
}
