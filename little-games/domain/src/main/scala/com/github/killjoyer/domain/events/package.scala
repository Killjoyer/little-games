package com.github.killjoyer.domain

package object events {

  sealed trait LittleGameEvent[A] {
    val payload: A
  }

  case class LobbyEvent(payload: LobbyPayload) extends LittleGameEvent[LobbyPayload]

  case class GameEvent(payload: BullsCowsPayload) extends LittleGameEvent[BullsCowsPayload]

  case class ChatEvent(payload: ChatPayload) extends LittleGameEvent[ChatPayload]

}
