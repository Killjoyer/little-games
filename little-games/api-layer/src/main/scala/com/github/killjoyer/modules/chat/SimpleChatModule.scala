package com.github.killjoyer.modules.chat
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.users.Username
import com.github.killjoyer.modules.AppEndpoint
import sttp.tapir.codec.newtype.codecForNewType
import sttp.tapir.endpoint
import sttp.tapir.ztapir._
import zio.ZLayer

case class SimpleChatModule(handler: SimpleChatHandler) {
  private val baseEndpoint =
    endpoint
      .tag("Chat")
      .in("api" / "chat")

  private val singInToChat: AppEndpoint =
    baseEndpoint.post
      .in("sign-in" / query[ChatId]("chatId") / query[Username]("username"))
      .zServerLogic((handler.registerToChat _).tupled)

  private val sendMessage: AppEndpoint =
    baseEndpoint.post
      .in("send" / query[ChatId]("chatId") / query[Username]("username") / query[String]("message"))
      .zServerLogic((handler.sendMessage _).tupled)

  val endpoints: List[AppEndpoint] = List(singInToChat, sendMessage)
}

object SimpleChatModule {
  val layer: ZLayer[SimpleChatHandler, Nothing, SimpleChatModule] = ZLayer.fromFunction(SimpleChatModule.apply _)
}
