ThisBuild / scalaVersion      := "2.13.8"
ThisBuild / version           := "0.1.0"
ThisBuild / organization      := "com.skunkworks"
ThisBuild / organizationName  := "skunkworks"
ThisBuild / scalacOptions    ++= Seq(
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

lazy val root = (project in file("."))
  .settings(
    name := "zio-auth0",
    libraryDependencies ++= Seq(
      "dev.zio"   %% "zio"      % "2.0.0",
      "com.auth0"  % "auth0"    % "1.42.0",
      "dev.zio"   %% "zio-test" % "2.0.0" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
