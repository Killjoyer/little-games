package com.killjoyer.infrastructure.config

import com.typesafe.config.ConfigFactory
import zio.config.ReadError
import zio.{&, ZEnvironment, ZIO, ZLayer}
import zio.config.magnolia.descriptor
import zio.config.typesafe.TypesafeConfig

object ConfigLoader {
  def load(): ZLayer[Any, ReadError[String], HttpServerConfig & DatabaseConfig] =
    TypesafeConfig
      .fromTypesafeConfig(ZIO.attempt(ConfigFactory.load()), descriptor[AppConfig])
      .map { env =>
        val appConfig = env.get
        ZEnvironment(appConfig.http).add(appConfig.db)
      }
}
