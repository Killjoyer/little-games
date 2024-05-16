import Dependencies._

ThisBuild / scalaVersion      := "2.13.10"
ThisBuild / organization      := "com.killjoyer"
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
    zio.test,
    zio.mock,
    zio.catsInterop,
    cats.core,
    circe.core,
    circe.generic,
    tofu.zioLogging,
    tofu.logging,
  )

lazy val dbLibraries = Seq(doobie.core, doobie.postgres)

lazy val httpLibraries = Seq(tapir.core, tapir.zio, tapir.zioHttpServer, tapir.swagger, tapir.circe)

lazy val domain = (project in file("little-games/domain"))
  .settings(name := "domain")

lazy val dataAccessLayer = (project in file("little-games/data-layer"))
  .dependsOn(domain)
  .settings(
    name                 := "data-layer",
    libraryDependencies ++= commonLibraries ++ dbLibraries,
    testFrameworks       += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val serviceLayer = (project in file("little-games/service-layer"))
  .dependsOn(domain, dataAccessLayer)
  .settings(
    name                 := "service-layer",
    libraryDependencies ++= commonLibraries,
    testFrameworks       += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val apiLayer = (project in file("little-games/api-layer"))
  .dependsOn(domain, serviceLayer)
  .settings(
    name                 := "api-layer",
    libraryDependencies ++= commonLibraries ++ httpLibraries,
    testFrameworks       += new TestFramework("zio.test.sbt.ZTestFramework"),
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
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )
