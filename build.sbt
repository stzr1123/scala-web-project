name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  jdbc,
  caffeine,
  ws,
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
  "org.postgresql" % "postgresql" % "42.2.5",
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.2",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
