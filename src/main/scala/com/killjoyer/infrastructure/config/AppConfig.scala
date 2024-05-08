package com.killjoyer.infrastructure.config

case class AppConfig(http: HttpServerConfig, db: DatabaseConfig)

case class HttpServerConfig(port: Int, host: String)

case class DatabaseConfig(url: String,  user: String, password: String)