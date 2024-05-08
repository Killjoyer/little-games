package com.killjoyer.modules.echo

import com.killjoyer.modules.echo.BullsAndCowsModule.{BullsAndCowsResponse, BullsAndCowsStartGameRequest, BullsAndCowsStartGameResponse, ServerError}
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import io.circe.generic.JsonCodec
import sttp.tapir.ztapir._
import io.circe.generic.auto._

import java.util.UUID
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import zio.ZLayer

case class BullsAndCowsModule(handler: BullsAndCowsHandler) {
  private val bcEndpoint = endpoint
    .in("api" / "bulls-cows")
    .tag("Bulls and Cows")
    .errorOut(jsonBody[ServerError])

  private val startGame: ZServerEndpoint[Any, Any] =
    bcEndpoint
      .post
      .in(jsonBody[BullsAndCowsStartGameRequest])
      .out(jsonBody[BullsAndCowsStartGameResponse])
      .zServerLogic(handler.bullsAndCowsStartGame)

  private val bullsAndCows: ZServerEndpoint[Any, Any] =
    bcEndpoint
      .get
      .in(query[String]("guess") / query[UUID]("gameId"))
      .out(jsonBody[BullsAndCowsResponse])
      .zServerLogic { case (guess, gameId) => handler.bullsAndCows(guess, gameId) }

  val endpoints: List[ZServerEndpoint[Any, Any]] = List(startGame, bullsAndCows)
}

object BullsAndCowsModule {
  @JsonCodec
  case class BullsAndCowsStartGameRequest(wordLength: Int, allowDuplicates: Boolean)

  @JsonCodec
  case class BullsAndCowsStartGameResponse(gameId: UUID)

  @JsonCodec
  case class BullsAndCowsResponse(gameId: UUID, result: BullsAndCowsResult, history: List[BullsAndCowsResult])

  @JsonCodec
  case class ServerError(code: String, message: String)

  val layer: ZLayer[BullsAndCowsHandler, Nothing, BullsAndCowsModule] = ZLayer.fromFunction(BullsAndCowsModule.apply _)
}
