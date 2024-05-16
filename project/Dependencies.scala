import sbt.*

object Dependencies {
  object zio {
    lazy val version     = "2.1.0"
    lazy val core        = "dev.zio" %% "zio"              % version
    lazy val test        = "dev.zio" %% "zio-test"         % version      % Test
    lazy val mock        = "dev.zio" %% "zio-mock"         % "1.0.0-RC12" % Test
    lazy val catsInterop = "dev.zio" %% "zio-interop-cats" % "23.1.0.2"

    lazy val configVersion  = "3.0.7"
    lazy val config         = "dev.zio" %% "zio-config"          % configVersion
    lazy val configTypesafe = "dev.zio" %% "zio-config-typesafe" % configVersion
    lazy val configMagnolia = "dev.zio" %% "zio-config-magnolia" % configVersion
  }

  object tapir {
    lazy val version = "1.10.7"

    lazy val core          = "com.softwaremill.sttp.tapir" %% "tapir-core"              % version
    lazy val zio           = "com.softwaremill.sttp.tapir" %% "tapir-zio"               % version
    lazy val zioHttpServer = "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server"   % version
    lazy val swagger       = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % version
    lazy val circe         = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % version
  }

  object circe {
    lazy val version = "0.14.7"
    lazy val core    = "io.circe" %% "circe-core"    % version
    lazy val generic = "io.circe" %% "circe-generic" % version
    lazy val parser  = "io.circe" %% "circe-parser"  % version
  }

  object tofu {
    lazy val version    = "0.13.1"
    lazy val zioLogging = "tf.tofu" %% "tofu-zio2-logging" % version
    lazy val logging    = "tf.tofu" %% "tofu-logging"      % version
  }

  object cats {
    lazy val version = "2.10.0"
    lazy val core    = "org.typelevel" %% "cats-core" % version
  }

  object grpc {
    lazy val netty   = "io.grpc"               % "grpc-netty"           % scalapb.compiler.Version.grpcJavaVersion
    lazy val runtime = "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
  }
}

object doobie {
  lazy val version  = "1.0.0-RC4"
  lazy val core     = "org.tpolecat" %% "doobie-core"     % version
  lazy val postgres = "org.tpolecat" %% "doobie-postgres" % version
}
