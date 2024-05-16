package com.github.killjoyer.infrastructure.config

final case class BuildConfig(http: HttpServerConfig, db: DatabaseConfig)

final case class HttpServerConfig(port: Int, host: String)

final case class DatabaseConfig(url: String, user: String, password: String)
