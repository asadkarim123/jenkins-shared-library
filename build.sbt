import Dependencies._

mainClass in assembly := Some("kata.bowling.BowlingUI")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.12",
      version      := "0.1",
      isSnapshot   := true
    )),
    name := "bowling-kata",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      junit % Test
    )
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) =>
    (xs map {_.toLowerCase}) match {
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") || ps.last.endsWith(".rsa") =>
        MergeStrategy.discard
      case _ => MergeStrategy.last
    }
  case x => MergeStrategy.last
}

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/junit")

publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.de-gbi.xyz", "test", "12345")

publishTo := {
  val nexus = "http://nexus.de-gbi.xyz/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "repository/maven-releases/")
  else
    Some("releases"  at nexus + "repository/maven-releases/")
}
