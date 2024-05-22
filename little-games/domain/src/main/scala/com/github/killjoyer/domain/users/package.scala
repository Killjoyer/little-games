package com.github.killjoyer.domain
import io.estatico.newtype.macros.newtype

package object users {
  @newtype case class Username(value: String)
}
