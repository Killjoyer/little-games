package com.github.killjoyer.services.impls

import com.github.killjoyer.services.traits.BullsAndCowsService.BullsAndCowsResult
import com.github.killjoyer.services.traits.BullsAndCowsSimultaneousMultiplayerService
import zio.Chunk
import zio.Hub
import zio.ZIO
import zio.concurrent.ConcurrentMap
import zio.stream.Stream
import zio.stream.ZSink
import zio.stream.ZStream

case class BullsAndCowsSimultaneousMultiplayerServiceLive(data: ConcurrentMap[String, Hub[String]])
    extends BullsAndCowsSimultaneousMultiplayerService {
  override def play(inputs: Stream[Throwable, String]): Stream[Throwable, BullsAndCowsResult] =
    ZStream.fromZIO(inputs.peel(ZSink.take(1)).flatMap { v =>
      Hub.unbounded[String].flatMap { hub =>
        v match {
          case (Chunk(s), tail) =>
            data
              .compute(
                s,
                {
                  case (k, Some(cur)) => Some(cur)
                  case (_, None)      => Some(hub)
                },
              )
              .flatMap {
                case Some(value) => tail.foreach(value.publish(_)).fork
                case None        => ZIO.fail(new RuntimeException("Invalid"))
              }
          case _ => ZIO.fail(new RuntimeException("invalid state"))
        }
      }
    })
}
