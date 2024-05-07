package com.killjoyer.modules.echo

import com.killjoyer.modules.echo.BullsAndCowsModule.{BullsAndCowsResponse, ServerError}
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import io.circe.generic.JsonCodec
import sttp.tapir.ztapir._
import io.circe.generic.auto._

import java.util.UUID
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import zio.ZLayer

case class BullsAndCowsModule(handler: BullsAndCowsHandler) {
  private val bcEndpoint = endpoint.tag("Bulls and Cows").errorOut(jsonBody[ServerError])

  private val bullsAndCows: ZServerEndpoint[Any, Any] =
    bcEndpoint
      .get
      .in("api" / "bulls-cows" / query[String]("guess") / query[Option[UUID]]("gameId"))
      .out(jsonBody[BullsAndCowsResponse])
      .zServerLogic { case (guess, gameId) => handler.bullsAndCows(guess, gameId) }

  val endpoints: List[ZServerEndpoint[Any, Any]] = List(bullsAndCows)
}

object BullsAndCowsModule {
  @JsonCodec
  case class BullsAndCowsResponse(gameId: UUID, result: BullsAndCowsResult, history: List[BullsAndCowsResult])

  @JsonCodec
  case class ServerError(code: String, message: String)

  val layer: ZLayer[BullsAndCowsHandler, Nothing, BullsAndCowsModule] = ZLayer.fromFunction(BullsAndCowsModule.apply _)
}
