package com.github.killjoyer.services.application.traits

import com.github.killjoyer.domain.events.LittleGamesEvent
import com.github.killjoyer.domain.users.UserId
import zio.RIO
import zio.Scope
import zio.Task
import zio.UIO
import zio.stream.Stream

trait UserEventsRouter {
  def registerRoute(inputs: Stream[Throwable, String]): RIO[Scope, Stream[Throwable, LittleGamesEvent]]

  def sendEvent(receiver: UserId, event: LittleGamesEvent): UIO[Boolean]

  def subscribeFor(user: UserId, events: Stream[Throwable, LittleGamesEvent]): Task[Unit]
}
