organization in ThisBuild := "com.co"
version in ThisBuild := "0.0.1"


// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false

lazy val `co-api` = (project in file("."))
  .aggregate(
    apidoc,
    `customer-api`,
    `customer-impl`
    
  )

lazy val apidoc = (project in file("apidoc"))
  .enablePlugins(PlayScala, LagomPlay, SbtReactiveAppPlugin)
  .settings(
    enableServiceDiscovery := true,
    libraryDependencies ++= Seq(
      guice, // This is required to configure Play's application loader
      ws
    ),
    httpIngressPaths := Seq("/")
  )

lazy val `customer-api` = (project in file("customer-api"))
  .settings(
    libraryDependencies += lagomScaladslApi
  )

lazy val `customer-impl` = (project in file("customer-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      guice, // This is required to configure Play's application loader
     jdbc,
      evolutions,
     "org.playframework.anorm" %% "anorm" % "2.6.1"
    )
  )
  .dependsOn(`customer-api`)

