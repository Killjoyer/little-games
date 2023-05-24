import sbt.*

object Dependencies {
  object zio {
    lazy val version = "2.0.13"
    lazy val core    = "dev.zio" %% "zio"      % version
    lazy val test    = "dev.zio" %% "zio-test" % version % Test
  }

  object tapir {
    lazy val version       = "1.4.0"
    lazy val core          = "com.softwaremill.sttp.tapir" %% "tapir-core"            % version
    lazy val zio           = "com.softwaremill.sttp.tapir" %% "tapir-zio"             % version
    lazy val zioHttpServer = "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % version
  }

  object circe {
    lazy val version = "0.14.5"
    lazy val core    = "io.circe" %% "circe-core" % version
  }

  object tofu {
    lazy val version    = "0.11.1"
    lazy val zioLogging = "tf.tofu" %% "tofu-zio2-logging" % version
    lazy val logging    = "tf.tofu" %% "tofu-logging"      % version
  }
}
