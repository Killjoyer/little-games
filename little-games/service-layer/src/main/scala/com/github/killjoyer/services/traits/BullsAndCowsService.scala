package com.github.killjoyer.services.traits

import com.github.killjoyer.services.traits.BullsAndCowsService.BullsAndCowsResult
import io.circe.generic.JsonCodec
import zio.Task

trait BullsAndCowsService {

  def getResult(guess: String, answer: String, allowDuplicates: Boolean): Task[BullsAndCowsResult]

  def generateWord(wordLength: Int, allowDuplicates: Boolean): Task[String]

}

object BullsAndCowsService {

  @JsonCodec
  final case class BullsAndCowsResult(guess: String, bulls: Int, cows: Int)

}
