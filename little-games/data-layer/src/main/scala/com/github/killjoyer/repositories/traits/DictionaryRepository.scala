package com.github.killjoyer.repositories.traits

import zio.Task

trait DictionaryRepository {

  def generateWord(length: Int, allowDuplicates: Boolean): Task[String]

}
