import Dependencies._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "com.killjoyer"

lazy val root = (project in file("."))
  .settings(
    name := "cartographers",
    scalacOptions ++= Seq("-Ywarn-unused"),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencies ++= Seq(
      zio.core,
      zio.test,
      zio.catsInterop,
      zio.config,
      zio.configTypesafe,
      zio.configMagnolia,
      tapir.core,
      tapir.zio,
      tapir.zioHttpServer,
      tapir.swagger,
      circe.core,
      tofu.zioLogging,
      tofu.logging,
      cats.core
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
