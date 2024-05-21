package com.github.killjoyer.domain

package object events {

  sealed trait LittleGameEvent[A] {
    val payload: A
  }

  case class Lobby(payload: LobbyPayload) extends LittleGameEvent[LobbyPayload]

  case class LobbyPayload()

  case class BullsCows(payload: BullsCowsPayload) extends LittleGameEvent[BullsCowsPayload]

  case class BullsCowsPayload()

}
