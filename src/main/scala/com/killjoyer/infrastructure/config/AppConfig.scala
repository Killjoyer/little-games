package com.killjoyer.infrastructure.config

case class AppConfig(http: HttpServerConfig)

case class HttpServerConfig(port: Int, host: String)
