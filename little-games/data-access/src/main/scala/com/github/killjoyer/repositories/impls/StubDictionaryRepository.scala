package com.github.killjoyer.repositories.impls

import com.github.killjoyer.repositories.traits.DictionaryRepository
import zio.Task
import zio.ULayer
import zio.ZIO
import zio.ZLayer

class StubDictionaryRepository extends DictionaryRepository {

  override def generateWord(length: Int, allowDuplicates: Boolean): Task[String] =
    ZIO.succeed("салон")
}

object StubDictionaryRepository {
  val layer: ULayer[DictionaryRepository] = ZLayer.succeed(new StubDictionaryRepository)
}
