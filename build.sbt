import Dependencies.*

ThisBuild / scalaVersion      := "2.13.10"
ThisBuild / organization      := "com.github.killjoyer"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalacOptions     := Seq("-Ywarn-unused", "-Ymacro-annotations", "-Wunused:imports")

Compile / PB.targets := Seq(
  scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value / "scalapb",
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb",
)

lazy val commonLibraries =
  Seq(
    zio.core,
    zio.stream,
    zio.concurrent,
    zio.test,
    zio.testSbt,
    zio.mock,
    zio.catsInterop,
    cats.core,
    circe.core,
    circe.generic,
    circe.extras,
    tofu.zioLogging,
    tofu.logging,
    utils.newtype,
  )

lazy val dbLibraries = Seq(doobie.core, doobie.postgres)

lazy val httpLibraries = Seq(tapir.core, tapir.zio, tapir.zioHttpServer, tapir.swagger, tapir.circe, tapir.newtype)

lazy val domain = (project in file("little-games/domain"))
  .settings(
    name                 := "domain",
    libraryDependencies ++= commonLibraries,
  )

lazy val serviceLayer = (project in file("little-games/service-layer"))
  .dependsOn(domain)
  .settings(
    name                 := "service-layer",
    libraryDependencies ++= commonLibraries,
  )

lazy val dataAccessLayer = (project in file("little-games/data-layer"))
  .dependsOn(domain, serviceLayer)
  .settings(
    name                 := "data-layer",
    libraryDependencies ++= commonLibraries ++ dbLibraries,
  )

lazy val apiLayer = (project in file("little-games/api-layer"))
  .dependsOn(domain, serviceLayer)
  .settings(
    name                 := "api-layer",
    libraryDependencies ++= commonLibraries ++ httpLibraries,
  )

lazy val app = (project in file("little-games/main"))
  .dependsOn(domain, dataAccessLayer, serviceLayer, apiLayer)
  .settings(
    name := "main",
    libraryDependencies ++=
      commonLibraries ++
        dbLibraries ++
        httpLibraries ++
        Seq(
          grpc.netty,
          grpc.runtime,
          zio.config,
          zio.configTypesafe,
          zio.configMagnolia,
        ),
//    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )
