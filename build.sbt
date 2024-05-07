import Dependencies._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "com.killjoyer"

Compile / PB.targets := Seq(
  scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value / "scalapb",
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb"
)

lazy val root = (project in file("."))
  .settings(
    name := "cartographers",
    scalacOptions ++= Seq("-Ywarn-unused", "Ymacro-annotations"),
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
