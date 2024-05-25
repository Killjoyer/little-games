package com.github.killjoyer.services.application.impls

import com.github.killjoyer.domain.events.LittleGamesEvent
import com.github.killjoyer.domain.users.UserId
import com.github.killjoyer.services.application.impls.UserEventsRouterLive.UserStorage
import com.github.killjoyer.services.application.traits.UserEventsRouter
import zio.Chunk
import zio.Hub
import zio.RIO
import zio.Ref
import zio.Scope
import zio.Task
import zio.UIO
import zio.ZIO
import zio.ZLayer
import zio.stream.Stream
import zio.stream.ZSink
import zio.stream.ZStream

case class UserEventsRouterLive(data: Ref.Synchronized[UserStorage]) extends UserEventsRouter {
  override def registerRoute(inputs: Stream[Throwable, String]): RIO[Scope, Stream[Throwable, LittleGamesEvent]] =
    data
      .modifyZIO(users =>
        inputs.peel(ZSink.take[String](1)).flatMap {
          // we dont care about user input, we use rest for this
          case (Chunk(user), _) =>
            val username = UserId(user)
            users.get(username) match {
              // reconnect
              case Some(hub) =>
                hub.shutdown *>
                  Hub
                    .unbounded[LittleGamesEvent]
                    .map(hub => (hub, users.updated(username, hub)))
              // newly connected user
              case None =>
                Hub
                  .unbounded[LittleGamesEvent]
                  .map(hub => (hub, users.updated(username, hub)))
            }
          // user didn't introduce himself
          case _ =>
            ZIO
              .fail(new RuntimeException("unauthorized"))
        }
      )
      .map(ZStream.fromHub(_))

  override def sendEvent(receiver: UserId, event: LittleGamesEvent): UIO[Boolean] =
    data.modifyZIO(users =>
      users
        .get(receiver)
        .fold(ZIO.logInfo(s"no user $receiver in storage").as(false))(hub => hub.publish(event))
        .map(r => (r, users))
    )

  override def subscribeFor(
                             user: UserId,
                             events: Stream[Throwable, LittleGamesEvent],
  ): Task[Unit] =
    data.updateZIO(users =>
      (users.get(user) match {
        case Some(hub) => events.foreach(hub.publish).forkDaemon.unit
        case None      => ZIO.fail(new RuntimeException("No such user"))
      }).as(users)
    )
}

object UserEventsRouterLive {
  type UserStorage = Map[UserId, Hub[LittleGamesEvent]]

  val layer: ZLayer[Any, Nothing, UserEventsRouter] =
    ZLayer.fromZIO(
      Ref.Synchronized.make(Map.empty[UserId, Hub[LittleGamesEvent]]).map(ref => UserEventsRouterLive(ref))
    )
}
