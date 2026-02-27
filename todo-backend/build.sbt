lazy val root = project
  .in(file("."))
  .settings(
    name := "todo-backend",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.7.1",
  )

libraryDependencies ++= Seq(
  "com.greenfossil" %% "thorium" % "0.10.0" withSources(),
  "org.slf4j" % "slf4j-api" % "2.0.17",
  "org.typelevel" %% "cats-core" % "2.13.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.greenfossil" %% "commons-json" % "1.3.2",
  "ch.qos.logback" % "logback-classic" % "1.5.18" % Test,
  "org.scalameta" %% "munit" % "1.2.1" % Test
)
