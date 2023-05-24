package com.killjoyer

import tofu.logging.zlogs._
import zio._

object Main extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> TofuZLogger.addToRuntime

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = ZIO.logInfo("Welcome to your first ZIO app!")
}
