version := "0.1.28"

name := "car_adverts_service"

maintainer := "terdong@gmail.com"

lazy val server = (project in file("server")).settings(commonSettings).settings(
  name := "car_adverts_service",
version := "0.1.28"
,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings"
  ),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    caffeine,
    guice,
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % "test",
    "org.mockito" % "mockito-core" % "2.27.0" % "test",
    "org.scanamo" %% "scanamo" % "0.10",
    "com.fasterxml.uuid" % "java-uuid-generator" % "3.2.0",
    "org.webjars" % "bootstrap" % "4.3.1"
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  //EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala).dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.7",
    "com.thoughtworks.binding" %%% "dom" % "11.7.0",
    "com.thoughtworks.binding" %%% "futurebinding" % "11.7.0",
    "com.typesafe.play" %%% "play-json" % "2.7.2"
  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

import sbt.Keys.publishArtifact
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += "com.typesafe.play" %%% "play-json" % "2.7.2"
  )
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  maintainer := "terdong@gmail.com",
  scalaVersion := "2.12.8",
  organization := "com.teamgehem",
  sources in(Compile, doc) := Seq.empty,
  publishArtifact in(Compile, packageDoc) := false
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen { s: State => "project server" :: s }