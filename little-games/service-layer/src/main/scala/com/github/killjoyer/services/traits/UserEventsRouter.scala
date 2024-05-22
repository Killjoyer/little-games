package com.github.killjoyer.services.traits

import com.github.killjoyer.domain.users.Username
import zio.RIO
import zio.Scope
import zio.Task
import zio.UIO
import zio.stream.Stream

trait UserEventsRouter {
  def registerRoute(inputs: Stream[Throwable, String]): RIO[Scope, Stream[Throwable, String]]

  def sendEvent(receiver: Username, event: String): UIO[Boolean]

  def subscribeFor(user: Username, events: Stream[Throwable, String]): Task[Unit]
}
