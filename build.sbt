version := "0.0.10"

name := "car_adverts_service"
      
lazy val `car_adverts_service` = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")
      
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(caffeine, ws, guice)

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % "test",
  "org.scanamo" %% "scanamo" % "1.0.0-M9",
  "com.gu" % "scanamo-testkit_2.12" % "1.0.0-M8",
  "com.fasterxml.uuid" % "java-uuid-generator" % "3.2.0",
  "org.webjars" % "bootstrap" % "4.3.1",
  "org.webjars" % "requirejs" % "2.2.0"
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

pipelineStages := Seq(rjs)
