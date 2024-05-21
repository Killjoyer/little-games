package com.github.killjoyer.services.traits

import zio.RIO
import zio.Scope
import zio.UIO
import zio.stream.Stream

trait UserEventsRouter {
  def registerRoute(inputs: Stream[Throwable, String]): RIO[Scope, Stream[Throwable, String]]

  def sendEvent(receiver: String, event: String): UIO[Boolean]
}
