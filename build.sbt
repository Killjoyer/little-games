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

lazy val commonLibraries = Seq(zio.core, zio.test, zio.mock, zio.catsInterop)

lazy val domain = (project in file("little-games/domain"))
  .settings(name := "domain")

lazy val dataAccessLayer = (project in file("little-games/data-access"))
  .dependsOn(domain)
  .settings(name := "data-access", libraryDependencies ++= commonLibraries ++ Seq(doobie.core, doobie.postgres))

lazy val app = (project in file("little-games/main"))
  .dependsOn(domain, dataAccessLayer)
  .settings(
    name := "little-games",
    libraryDependencies ++=
      commonLibraries ++
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
        tapir.circe,
        circe.core,
        tofu.zioLogging,
        tofu.logging,
        cats.core
      ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
