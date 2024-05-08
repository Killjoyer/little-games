package com.killjoyer.repositories.impls

import cats.implicits.catsSyntaxEq
import com.killjoyer.repositories.traits.DictionaryRepository
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import doobie.implicits._
import zio.{&, durationInt, Random, Task, ZIO, ZLayer}
import zio.interop.catz.concurrentInstance

class RuDbDictionaryRepository(transactor: Transactor[Task], random: Random, words: Task[Vector[String]]) extends DictionaryRepository {
  override def generateWord(length: Int, allowDuplicates: Boolean): Task[String] =
    words.flatMap { v =>
      val suitableWords = v.filter(s => s.length === length && (allowDuplicates || s.toSet.size === length))
      random.nextIntBounded(suitableWords.size).map(suitableWords)
    }
}

object RuDbDictionaryRepository {
  val layer: ZLayer[Transactor[Task] & Random, Nothing, DictionaryRepository] =
    ZLayer.fromZIO(for {
      tr    <- ZIO.service[Transactor[Task]]
      rnd   <- ZIO.service[Random]
      words <- getWords(tr).cached(30.minutes)
    } yield new RuDbDictionaryRepository(tr, rnd, words))

  private def getWords(transactor: Transactor[Task]) =
    sql"SELECT word FROM ru_dictionary"
      .query[String]
      .to[Vector]
      .transact(transactor)
}
