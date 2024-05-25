package com.github.killjoyer.repositories

import zio.Task

trait DictionaryRepository {

  def generateWord(length: Int, allowDuplicates: Boolean): Task[String]

}
