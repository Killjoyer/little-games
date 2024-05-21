package com.github.killjoyer.services.impls

import cats.implicits.catsSyntaxEq

import com.github.killjoyer.repositories.DictionaryRepository
import com.github.killjoyer.services.traits.BullsAndCowsService
import com.github.killjoyer.services.traits.BullsAndCowsService.BullsAndCowsResult
import zio.Task
import zio.ZIO
import zio.ZLayer

final case class BullsAndCowsServiceLive(wordsRepo: DictionaryRepository) extends BullsAndCowsService {

  private def isWordValid(guess: String, allowDuplicates: Boolean, wordLength: Int): Task[Boolean] =
    ZIO.succeed(
      guess.length === wordLength && (allowDuplicates || guess.toSet.size === wordLength) // todo
    )

  private def dropAt(s: String)(i: Int): String = s.take(i) + s.drop(i + 1)

  override def getResult(guess: String, answer: String, allowDuplicates: Boolean): Task[BullsAndCowsResult] = {
    lazy val (bulls, guessWithNoBulls, answerWithNoBulls) =
      (0 until guess.length)
        .foldLeft((0, guess, answer)) {
          case ((bulls, gs, ans), i) if answer.charAt(i) === guess.charAt(i) =>
            (bulls + 1, dropAt(gs)(i), dropAt(ans)(i))
          case (acc, _) => acc
        }

    lazy val cows = guessWithNoBulls.foldLeft(0) {
      case (cows, e) if answerWithNoBulls.contains(e) => cows + 1
      case (cows, _)                                  => cows
    }

    ZIO.ifZIO(isWordValid(guess, allowDuplicates, answer.length))(
      ZIO.succeed(BullsAndCowsResult(guess, bulls, cows)),
      ZIO.fail(new RuntimeException("invalid word")) // todo
    )
  }

  override def generateWord(wordLength: Int, allowDuplicates: Boolean): Task[String] =
    wordsRepo.generateWord(wordLength, allowDuplicates)

}

object BullsAndCowsServiceLive {

  val layer: ZLayer[DictionaryRepository, Nothing, BullsAndCowsService] =
    ZLayer.fromFunction(BullsAndCowsServiceLive.apply _)

}
