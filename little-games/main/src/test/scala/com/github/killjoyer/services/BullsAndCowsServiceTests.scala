package com.github.killjoyer.services

import com.killjoyer.mocks.MockDictionaryRepository
import com.killjoyer.services.BullsAndCowsService.BullsAndCowsResult
import com.killjoyer.services.impls.BullsAndCowsServiceLive
import zio.test.Assertion.equalTo
import zio.{Scope, ZIO}
import zio.test._

import scala.tools.nsc.tasty.SafeEq

object BullsAndCowsServiceTests extends ZIOSpecDefault {
  def useService[R, E, A](f: BullsAndCowsService => ZIO[R, E, A]): ZIO[BullsAndCowsService with R, E, A] =
    ZIO.service[BullsAndCowsService].flatMap(f(_))

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Bulls and Cows service tests")(test("empty result") {
      useService(_.getResult("fghij", "abcde", allowDuplicates = false))
        .provide(MockDictionaryRepository.empty >>> BullsAndCowsServiceLive.layer)
        .map(v => assertTrue(v === BullsAndCowsResult("fghij", 0, 0)))
    })
}
