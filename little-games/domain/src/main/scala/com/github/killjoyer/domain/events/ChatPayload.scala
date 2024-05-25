package com.github.killjoyer.domain.events

import com.github.killjoyer.domain.users.UserId

sealed trait ChatPayload

object ChatPayload {

  case class SendMessage(author: UserId, text: String) extends ChatPayload

  case class UserJoined(user: UserId) extends ChatPayload
}
