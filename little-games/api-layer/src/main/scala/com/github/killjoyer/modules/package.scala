package com.github.killjoyer
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint

package object modules {
  type AppEndpoint = ZServerEndpoint[Any, ZioStreams with WebSockets]
}
