name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  jdbc,
  caffeine,
  ws,
  evolutions,
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
  "org.postgresql" % "postgresql" % "42.2.5",
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.2",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % "test",
  "org.mockito" % "mockito-core" % "2.7.22" % "test"
)

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
