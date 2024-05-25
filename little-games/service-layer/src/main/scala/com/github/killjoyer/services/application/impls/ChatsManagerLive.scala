package com.github.killjoyer.services.application.impls

import com.github.killjoyer.domain.chats.Chat
import com.github.killjoyer.domain.chats.Chat.ChatId
import com.github.killjoyer.domain.events.ChatPayload.SendMessage
import com.github.killjoyer.domain.events.ChatPayload.UserJoined
import com.github.killjoyer.domain.events.LittleGamesEvent.ChatEvent
import com.github.killjoyer.domain.users.UserId
import com.github.killjoyer.services.application.impls.ChatsManagerLive.ChatDoesntExist
import com.github.killjoyer.services.application.impls.ChatsManagerLive.UserDoesNotBelongToChat
import com.github.killjoyer.services.application.traits.ChatsManager
import com.github.killjoyer.services.application.traits.UserEventsRouter
import zio._
import zio.stream._

case class ChatsManagerLive(userEventsRouter: UserEventsRouter, chatsRef: Ref.Synchronized[Map[ChatId, Chat]])
    extends ChatsManager {

  override def registerToChat(username: UserId, chatId: ChatId): Task[Unit] =
    ZIO.logInfo(s"User $username wants to join chat $chatId") *>
      chatsRef.modifyZIO { chats =>
        chats
          .get(chatId)
          .fold(Hub.unbounded[ChatEvent].map(hub => Chat(chatId, hub, Set(username))))(chat =>
            ZIO.succeed(chat.copy(members = chat.members + username))
          )
          .map(v => (v, chats.updated(chatId, v)))
      }
        .tap(chat => userEventsRouter.subscribeFor(username, ZStream.fromHub(chat.events)))
        .tap(_.events.publish(ChatEvent(chatId, UserJoined(username)))) *>
      ZIO.logInfo(s"User $username added to chat $chatId")

  override def sendMessageToChat(
                                  username: UserId,
                                  chatId: ChatId,
                                  message: String,
  ): Task[Unit] =
    chatsRef.get
      .flatMap(chats =>
        chats.get(chatId) match {
          case Some(chat) if chat.members.contains(username) =>
            chat.events.publish(ChatEvent(chatId, SendMessage(username, message))).unit
          case Some(_) => ZIO.fail(UserDoesNotBelongToChat(chatId, username))
          case None    => ZIO.fail(ChatDoesntExist(chatId))
        }
      )
}

object ChatsManagerLive {

  val layer: ZLayer[UserEventsRouter, Nothing, ChatsManagerLive] = ZLayer.fromZIO(
    for {
      ref    <- Ref.Synchronized.make(Map.empty[ChatId, Chat])
      router <- ZIO.service[UserEventsRouter]
    } yield ChatsManagerLive(router, ref)
  )

  sealed trait ChatError extends Throwable

  case class ChatDoesntExist(chatId: ChatId) extends ChatError

  case class UserDoesNotBelongToChat(chatId: ChatId, username: UserId) extends ChatError

}
