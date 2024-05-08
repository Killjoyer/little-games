package com.killjoyer.infrastructure.database

import com.killjoyer.infrastructure.config.DatabaseConfig
import doobie.util.transactor.Transactor
import zio.{Task, ZIO, ZLayer}
import zio.interop.catz.asyncInstance

object TransactorProvider {
  def transactorLayer: ZLayer[DatabaseConfig, Nothing, Transactor[Task]] =
    ZLayer.fromZIO(for {
      config <- ZIO.service[DatabaseConfig]
      tr = Transactor.fromDriverManager[Task](
        driver = "org.postgresql.Driver",
        url = config.url,
        user = config.user,
        password = config.password,
        logHandler = None
      )
    } yield tr)
}
