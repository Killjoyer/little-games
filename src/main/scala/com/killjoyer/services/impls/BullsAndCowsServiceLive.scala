package com.killjoyer.services.impls

import cats.implicits.catsSyntaxEq
import com.killjoyer.repositories.traits.DictionaryRepository
import com.killjoyer.services.BullsAndCowsService
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import zio.{Task, ZIO, ZLayer}

import scala.annotation.tailrec

case class BullsAndCowsServiceLive(wordsRepo: DictionaryRepository) extends BullsAndCowsService {
  private def isWordValid(guess: String): Task[Boolean] = ZIO.succeed(true) // todo change

  override def getResult(guess: String, answer: String): Task[BullsAndCowsResult] = {
    @tailrec
    def count(answer: List[(Char, Int)])(guess: List[(Char, Int)])(bulls: Int, cows: Int): (Int, Int) =
      guess match {
        case e :: next if answer.contains(e) => count(answer)(next)(bulls + 1, cows)
        case (char, index) :: next if answer.exists { case (c, i) => c === char && i =!= index } =>
          count(answer)(next)(bulls, cows + 1)
        case _ :: next                       => count(answer)(next)(bulls, cows)
        case Nil                             => (bulls, cows)
      }

    val guessZipped  = guess.zipWithIndex.toList
    val answerZipped = answer.zipWithIndex.toList

    val (bulls, cows) = count(answerZipped)(guessZipped)(0, 0)

    ZIO.ifZIO(isWordValid(guess))(ZIO.succeed(BullsAndCowsResult(guess, bulls, cows)), ZIO.fail(new RuntimeException("invalid word")))
  }

  override def generateWord: Task[String] = wordsRepo.generateWord(5, allowDuplicates = false)
}

object BullsAndCowsServiceLive {
  val layer: ZLayer[DictionaryRepository, Nothing, BullsAndCowsService] = ZLayer.fromFunction(BullsAndCowsServiceLive.apply _)
}
