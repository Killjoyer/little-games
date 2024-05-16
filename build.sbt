import Dependencies._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "com.killjoyer"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalacOptions := Seq("-Ywarn-unused", "-Ymacro-annotations", "-Wunused:imports")

Compile / PB.targets := Seq(
  scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value / "scalapb",
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb"
)

lazy val commonLibraries =
  Seq(zio.core, zio.test, zio.mock, zio.catsInterop, cats.core, circe.core, circe.generic, tofu.zioLogging, tofu.logging)

lazy val dbLibraries = Seq(doobie.core, doobie.postgres)

lazy val domain = (project in file("little-games/domain"))
  .settings(name := "domain")

lazy val dataAccessLayer = (project in file("little-games/data-layer"))
  .dependsOn(domain)
  .settings(name := "data-layer", libraryDependencies ++= commonLibraries ++ dbLibraries)

lazy val serviceLayer = (project in file("little-games/service-layer"))
  .dependsOn(domain, dataAccessLayer)
  .settings(name := "service-layer", libraryDependencies ++= commonLibraries)

lazy val app = (project in file("little-games/main"))
  .dependsOn(domain, dataAccessLayer, serviceLayer)
  .settings(
    name := "main",
    libraryDependencies ++=
      commonLibraries ++
      dbLibraries ++
      Seq(
        grpc.netty,
        grpc.runtime,
        zio.config,
        zio.configTypesafe,
        zio.configMagnolia,
        tapir.core,
        tapir.zio,
        tapir.zioHttpServer,
        tapir.swagger,
        tapir.circe
      ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
