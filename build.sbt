version := "0.0.3"

name := "car_adverts_service"
      
lazy val `car_adverts_service` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(caffeine, ws, guice)

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % "test",
  "org.scanamo" %% "scanamo" % "1.0.0-M9",
  "com.gu" % "scanamo-testkit_2.12" % "1.0.0-M8"
)

unmanagedResourceDirectories in Test += baseDirectory.value / "target/web/public/test"

      