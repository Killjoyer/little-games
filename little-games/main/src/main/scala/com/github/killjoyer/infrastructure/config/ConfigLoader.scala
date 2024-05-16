package com.github.killjoyer.infrastructure.config

import com.typesafe.config.ConfigFactory
import zio.&
import zio.Config
import zio.ZEnvironment
import zio.ZLayer
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

object ConfigLoader {

  def load(): ZLayer[Any, Config.Error, HttpServerConfig & DatabaseConfig] =
    ZLayer
      .fromZIO(
        TypesafeConfigProvider
          .fromTypesafeConfig(ConfigFactory.load())
          .load(deriveConfig[AppConfig])
      )
      .map { env =>
        val appConfig = env.get
        ZEnvironment(appConfig.build.http).add(appConfig.build.db)
      }

}
