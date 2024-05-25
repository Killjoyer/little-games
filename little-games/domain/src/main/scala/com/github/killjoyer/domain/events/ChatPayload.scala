package com.github.killjoyer.domain.events

import com.github.killjoyer.domain.users.Username

sealed trait ChatPayload

object ChatPayload {

  case class SendMessage(author: Username, text: String) extends ChatPayload

  case class UserJoined(user: Username) extends ChatPayload
}
