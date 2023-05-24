import Dependencies.*

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "com.killjoyer"

lazy val root = (project in file("."))
  .settings(
    name := "cartographers",
    scalacOptions ++= Seq("-Ywarn-unused"),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencies ++= Seq(zio.core, zio.test, tapir.core, tapir.zio, tapir.zioHttpServer, circe.core, tofu.zioLogging, tofu.logging),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
