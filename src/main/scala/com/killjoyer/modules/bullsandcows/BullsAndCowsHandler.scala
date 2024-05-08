package com.killjoyer.modules.echo

import com.killjoyer.modules.echo.BullsAndCowsHandler.BullsAndCowsState
import com.killjoyer.modules.echo.BullsAndCowsModule.{BullsAndCowsResponse, BullsAndCowsStartGameRequest, BullsAndCowsStartGameResponse, ServerError}
import com.killjoyer.services.BullsAndCowsService
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import zio.{IO, Random, Ref, ZIO, ZLayer}

import java.util.UUID

case class BullsAndCowsHandler(service: BullsAndCowsService, random: Random, gamesRef: Ref.Synchronized[Map[UUID, BullsAndCowsState]]) {
  def bullsAndCows(guess: String, gameId: UUID): IO[ServerError, BullsAndCowsResponse] =
    gamesRef.modifyZIO { games =>
      games
        .get(gameId)
        .fold[IO[ServerError, (BullsAndCowsResponse, Map[UUID, BullsAndCowsState])]](
          ZIO.fail(ServerError("WRONG_GAME_ID", "Game with this ID not found"))
        ) { state =>
          service
            .getResult(guess, state.answer, state.allowDuplicates)
            .mapBoth(
              th => ServerError("INTERNAL_ERROR", th.getMessage),
              result =>
                (BullsAndCowsResponse(gameId, result, state.history), games.updated(gameId, state.copy(history = result :: state.history)))
            )
        }
    }

  def bullsAndCowsStartGame(request: BullsAndCowsStartGameRequest): IO[ServerError, BullsAndCowsStartGameResponse] =
    gamesRef
      .modifyZIO(games =>
        for {
          word <- service.generateWord(request.wordLength, request.allowDuplicates)
          uuid <- random.nextUUID
        } yield (BullsAndCowsStartGameResponse(uuid), games.updated(uuid, BullsAndCowsState(word, request.allowDuplicates, List())))
      )
      .mapError(th => ServerError("INTERNAL_ERROR", th.getMessage))

}

object BullsAndCowsHandler {
  case class BullsAndCowsState(answer: String, allowDuplicates: Boolean, history: List[BullsAndCowsResult])

  val layer: ZLayer[Random with BullsAndCowsService, Nothing, BullsAndCowsHandler] =
    ZLayer.fromZIO(for {
      srv    <- ZIO.service[BullsAndCowsService]
      random <- ZIO.service[Random]
      ref    <- Ref.Synchronized.make(Map.empty[UUID, BullsAndCowsState])
    } yield BullsAndCowsHandler(srv, random, ref))
}
