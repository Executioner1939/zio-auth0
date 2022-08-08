import sbt.Keys.publishMavenStyle
import ReleaseTransformations._
import sbt.url
import sbtrelease.Version
import xerial.sbt.Sonatype.GitHubHosting

lazy val global = Seq(
  scalaVersion     := "2.13.8",
  organization     := "io.bitlevel",
  organizationName := "bitlevel",
  scalacOptions    ++= Seq(
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
  ),

  // SBT Sonatype
  sonatypeProfileName       := "io.bitlevel",
  publishTo                 := sonatypePublishToBundle.value,
  publishConfiguration      := publishConfiguration.value.withOverwrite(true),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
  publishMavenStyle         := true,
  licenses                  := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  sonatypeProjectHosting    := Some(GitHubHosting("Executioner1939", "zio-auth0", "shadowrhyder@gmail.com")),
  developers                := List(
    Developer(
      id    = "Executioner1939",
      name  = "Richard Peters",
      email = "shadowrhyder@gmail.com",
      url   = url("https://github.com/Executioner1939")
    )
  ),

  // SBT Release
  releaseCommitMessage := s"[skip ci] Setting version to ${(ThisBuild / version).value}",
  releaseNextCommitMessage := s"[skip ci] Setting version to ${(ThisBuild / version).value}",
  releaseIgnoreUntrackedFiles := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseCrossBuild := false,
  releaseVersionBump := Version.Bump.Minor,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    setReleaseVersion,
    tagRelease,
    pushChanges,
    releaseStepCommandAndRemaining("publishSigned"),
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  ),

  // Global Compiler Settings
  addCompilerPlugin ("com.olegpy" %% "better-monadic-for" % "0.3.1"),

  // Testing Frameworks
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
)


val zio: Seq[ModuleID] = {
  val version = "2.0.0"

  Seq(
    "dev.zio" %% "zio"          % version,
    "dev.zio" %% "zio-test"     % version % Test,
    "dev.zio" %% "zio-test-sbt" % version % Test
  )
}

val auth0: Seq[ModuleID] = Seq(
  "com.auth0" % "auth0" % "1.42.0"
)

lazy val `zio-auth0` = (project in file("./zio-auth0"))
  .settings(global: _*)
  .settings(libraryDependencies ++= zio ++ auth0)
  .settings(name := "zio-auth0", publishArtifact := true)

// ---------------------------------------------- //
// Project and configuration for the root project //
// ---------------------------------------------- //
lazy val root = (project in file("."))
  .settings(global: _*)
  .settings(publishArtifact := false)
  .settings(publish / skip := true)
  .aggregate(`zio-auth0`)