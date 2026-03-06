I woulazy val root = project
  .in(file("."))
  .settings(
    name := "todo-backend",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.7.1",
  )

assembly / assemblyMergeStrategy := {
  case PathList("module-info.class")     => MergeStrategy.discard
  case PathList("META-INF", _ @_*)       => MergeStrategy.discard
  case x                                 => (assembly / assemblyMergeStrategy).value(x)
}

libraryDependencies ++= Seq(
  "com.greenfossil" %% "thorium" % "0.10.0" withSources(),
  "com.greenfossil" %% "commons-json" % "1.3.2",
  "com.greenfossil" %% "data-mapping" % "1.3.7",
  "org.slf4j" % "slf4j-api" % "2.0.17",
  "org.typelevel" %% "cats-core" % "2.13.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.auth0" % "java-jwt" % "4.5.1",
  "com.typesafe.slick" %% "slick" % "3.6.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.6.1",
  "org.mariadb.jdbc" % "mariadb-java-client" % "3.5.1",
  "ch.qos.logback" % "logback-classic" % "1.5.18",
  "org.scalameta" %% "munit" % "1.2.1" % Test
)
