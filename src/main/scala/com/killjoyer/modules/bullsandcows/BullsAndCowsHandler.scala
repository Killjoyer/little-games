package com.killjoyer.modules.echo

import com.killjoyer.modules.echo.BullsAndCowsHandler.BullsAndCowsState
import com.killjoyer.modules.echo.BullsAndCowsModule.{BullsAndCowsResponse, ServerError}
import com.killjoyer.services.BullsAndCowsService
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import zio.{IO, Random, Ref, ZIO, ZLayer}

import java.util.UUID

case class BullsAndCowsHandler(service: BullsAndCowsService, random: Random, gamesRef: Ref.Synchronized[Map[UUID, BullsAndCowsState]]) {
  def bullsAndCows(guess: String, gameId: Option[UUID]): IO[ServerError, BullsAndCowsResponse] =
    gamesRef.modifyZIO { games =>
      gameId.fold {
        (
          for {
            word   <- service.generateWord
            uuid   <- random.nextUUID
            result <- service.getResult(guess.toLowerCase, word)
          } yield (BullsAndCowsResponse(uuid, result, List()), games.updated(uuid, BullsAndCowsState(word, List(result))))
        )
          .mapError(th => ServerError("INTERNAL_ERROR", th.getMessage))
      } { uuid =>
        games
          .get(uuid)
          .fold[IO[ServerError, (BullsAndCowsResponse, Map[UUID, BullsAndCowsState])]](
            ZIO.fail(ServerError("WRONG_GAME_ID", "Game with this ID not found"))
          ) { state =>
            service
              .getResult(guess, state.answer)
              .mapBoth(
                th => ServerError("INTERNAL_ERROR", th.getMessage),
                result =>
                  (BullsAndCowsResponse(uuid, result, state.history), games.updated(uuid, state.copy(history = result :: state.history)))
              )
          }
      }
    }
}

object BullsAndCowsHandler {
  case class BullsAndCowsState(answer: String, history: List[BullsAndCowsResult])

  val layer: ZLayer[Random with BullsAndCowsService, Nothing, BullsAndCowsHandler] =
    ZLayer.fromZIO(for {
      srv    <- ZIO.service[BullsAndCowsService]
      random <- ZIO.service[Random]
      ref    <- Ref.Synchronized.make(Map.empty[UUID, BullsAndCowsState])
    } yield BullsAndCowsHandler(srv, random, ref))
}
