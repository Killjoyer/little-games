package com.github.killjoyer

import com.github.killjoyer.repositories.traits.DictionaryRepository
import zio.mock.mockable

package object mocks {

  @mockable[DictionaryRepository]
  object MockDictionaryRepository
}
