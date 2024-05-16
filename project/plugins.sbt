addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.12.1")
addSbtPlugin("com.thesamet"  % "sbt-protoc"   % "1.0.2")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

libraryDependencies += "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.6.1"
