package com.killjoyer.repositories.impls

import com.killjoyer.repositories.traits.DictionaryRepository
import zio.{Task, ULayer, ZIO, ZLayer}

class StubDictionaryRepository extends DictionaryRepository {

  override def generateWord(length: Int, allowDuplicates: Boolean): Task[String] =
    ZIO.succeed("салон")
}

object StubDictionaryRepository {
  val layer: ULayer[DictionaryRepository] = ZLayer.succeed(new StubDictionaryRepository)
}
