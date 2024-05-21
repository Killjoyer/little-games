package com.github.killjoyer.repositories.impls

import cats.implicits.catsSyntaxEq

import com.github.killjoyer.repositories.DictionaryRepository
import doobie.implicits._
import doobie.util.transactor.Transactor
import zio.&
import zio.Random
import zio.Task
import zio.ZIO
import zio.ZLayer
import zio.durationInt
import zio.interop.catz.concurrentInstance

class RuDbDictionaryRepository(random: Random, words: Task[Vector[String]]) extends DictionaryRepository {

  override def generateWord(length: Int, allowDuplicates: Boolean): Task[String] =
    words.flatMap { v =>
      val suitableWords = v.filter(s => s.length === length && (allowDuplicates || s.toSet.size === length))
      random.nextIntBounded(suitableWords.size).map(suitableWords)
    }

}

object RuDbDictionaryRepository {

  val layer: ZLayer[Transactor[Task] & Random, Nothing, DictionaryRepository] =
    ZLayer.fromZIO(for {
      tr <- ZIO.service[Transactor[Task]]
      rnd <- ZIO.service[Random]
      words <- getWords(tr).cached(30.minutes)
    } yield new RuDbDictionaryRepository(rnd, words))

  private def getWords(transactor: Transactor[Task]) =
    sql"SELECT word FROM ru_dictionary"
      .query[String]
      .to[Vector]
      .transact(transactor)

}
