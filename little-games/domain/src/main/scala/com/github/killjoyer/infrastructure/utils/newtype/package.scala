package com.github.killjoyer.infrastructure.utils
import io.circe.Decoder
import io.circe.Encoder
import io.estatico.newtype.Coercible

package object newtype {
  implicit def newtypeEncoder[A, B](implicit ev: Coercible[A, B], enc: Encoder[B]): Encoder[A] =
    enc.contramap(ev(_))

  implicit def newtypeDecoder[A, B](implicit ev: Coercible[B, A], dec: Decoder[B]): Decoder[A] =
    dec.map(ev(_))
}
