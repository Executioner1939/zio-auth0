import sbt.Keys.publishMavenStyle
import ReleaseTransformations._
import sbt.url
import sbtrelease.Version
import xerial.sbt.Sonatype.GitHubHosting

// Global Settings
ThisBuild / name              := "zio-auth0"
ThisBuild / scalaVersion      := "2.13.8"
ThisBuild / version           := "0.1.0"
ThisBuild / organization      := "io.bitlevel"
ThisBuild / organizationName  := "bitlevel"
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

// Libraries and Frameworks
ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
ThisBuild / libraryDependencies ++= Seq(
  "dev.zio"   %% "zio"      % "2.0.0",
  "com.auth0"  % "auth0"    % "1.42.0",
  "dev.zio"   %% "zio-test" % "2.0.0" % Test
)

// SBT Sonatype
ThisBuild / sonatypeProfileName       := "io.bitlevel"
ThisBuild / publishTo                 := sonatypePublishToBundle.value
ThisBuild / publishConfiguration      := publishConfiguration.value.withOverwrite(true)
ThisBuild / publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
ThisBuild / publishMavenStyle         := true
ThisBuild / licenses                  := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / sonatypeProjectHosting    := Some(GitHubHosting("Executioner1939", "zio-auth0", "shadowrhyder@gmail.com"))
ThisBuild / developers                := List(
  Developer(
    id    = "Executioner1939",
    name  = "Richard Peters",
    email = "shadowrhyder@gmail.com",
    url   = url("https://github.com/Executioner1939")
  )
)

 // SBT Release
ThisBuild / publishArtifact               := true
ThisBuild / releaseCommitMessage          := s"[skip ci] Setting version to ${(ThisBuild / version).value}"
ThisBuild / releaseNextCommitMessage      := s"[skip ci] Setting version to ${(ThisBuild / version).value}"
ThisBuild / releaseIgnoreUntrackedFiles   := true
ThisBuild / releasePublishArtifactsAction := PgpKeys.publishSigned.value
ThisBuild / releaseCrossBuild             := false
ThisBuild / releaseVersionBump            := Version.Bump.Minor
ThisBuild / releaseProcess                := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  tagRelease,
  pushChanges,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

// Global Compiler Settings
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")