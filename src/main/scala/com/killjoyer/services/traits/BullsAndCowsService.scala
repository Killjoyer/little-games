package com.killjoyer.services

import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import io.circe.generic.JsonCodec
import zio.Task

trait BullsAndCowsService {
  def getResult(guess: String, answer: String): Task[BullsAndCowsResult]
  def generateWord: Task[String]
}

 object BullsAndCowsService {
   @JsonCodec
   case class BullsAndCowsResult(guess: String, bulls: Int, cows: Int)
 }