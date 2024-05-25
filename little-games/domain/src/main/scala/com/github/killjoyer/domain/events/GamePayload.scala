package com.github.killjoyer.domain.events

sealed trait GamePayload

object GamePayload {
  case class GameStarted() extends GamePayload
}
