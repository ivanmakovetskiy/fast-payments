ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.10"

val circeVersion = "0.14.1"
val akkaVersion = "2.6.18"
val akkaHttpVersion = "10.2.7"
val AkkaHttpJsonVersion = "1.39.2"

lazy val root = (project in file("."))
  .settings(
    name := "fast-payments",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % AkkaHttpJsonVersion
    )
  )