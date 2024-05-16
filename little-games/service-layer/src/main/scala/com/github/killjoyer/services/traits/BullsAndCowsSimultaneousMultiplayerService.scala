package com.github.killjoyer.services.traits

import com.github.killjoyer.services.traits.BullsAndCowsService.BullsAndCowsResult
import zio.stream.Stream

trait BullsAndCowsSimultaneousMultiplayerService {
  def play(inputs: Stream[Throwable, String]): Stream[Throwable, BullsAndCowsResult ]
}


