package com.github.killjoyer.modules.bullsandcows

import java.util.UUID

import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule.BullsAndCowsResponse
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule.BullsAndCowsStartGameRequest
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule.BullsAndCowsStartGameResponse
import com.github.killjoyer.modules.bullsandcows.BullsAndCowsModule.ServerError
import com.github.killjoyer.services.traits.BullsAndCowsService.BullsAndCowsResult
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.ztapir._
import zio.ZLayer

final case class BullsAndCowsModule(handler: BullsAndCowsHandler) {

  private val bcEndpoint = endpoint
    .in("api" / "bulls-cows")
    .tag("Bulls and Cows")
    .errorOut(jsonBody[ServerError])

  private val startGame: ZServerEndpoint[Any, Any] =
    bcEndpoint.post
      .in("start")
      .in(jsonBody[BullsAndCowsStartGameRequest])
      .out(jsonBody[BullsAndCowsStartGameResponse])
      .zServerLogic(handler.bullsAndCowsStartGame)

  private val bullsAndCows: ZServerEndpoint[Any, Any] =
    bcEndpoint.post
      .in(query[String]("guess") / query[UUID]("gameId"))
      .out(jsonBody[BullsAndCowsResponse])
      .zServerLogic { case (guess, gameId) => handler.bullsAndCows(guess, gameId) }

  val endpoints: List[ZServerEndpoint[Any, Any]] = List(startGame, bullsAndCows)

}

object BullsAndCowsModule {

  @JsonCodec
  final case class BullsAndCowsStartGameRequest(wordLength: Int, allowDuplicates: Boolean)

  @JsonCodec
  final case class BullsAndCowsStartGameResponse(gameId: UUID)

  @JsonCodec
  final case class BullsAndCowsResponse(gameId: UUID, result: BullsAndCowsResult, history: List[BullsAndCowsResult])

  @JsonCodec
  final case class ServerError(code: String, message: String)

  val layer: ZLayer[BullsAndCowsHandler, Nothing, BullsAndCowsModule] = ZLayer.fromFunction(BullsAndCowsModule.apply _)

}
