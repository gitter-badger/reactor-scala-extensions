import sbt.Keys.{autoAPIMappings, scalacOptions}
import scoverage.ScoverageKeys.coverageMinimum

// Defining dependencies
val reactorVersion = "3.3.5.RELEASE"
val reactorCore = "io.projectreactor" % "reactor-core" % reactorVersion
val scalaCollCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
//test libraries
val mockitoInline = "org.mockito" % "mockito-inline" % "3.3.3" % Test
val mockitoScala = "org.mockito" %% "mockito-scala" % "1.14.3" % Test
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.2" % Test
val reactorTest = "io.projectreactor" % "reactor-test" % reactorVersion % Test
val micrometer = "io.micrometer" % "micrometer-core" % "latest.release" % Test

//Scala versions for cross compiling
lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.2"
lazy val supportedScalaVersions = List(scala211, scala212, scala213)

ThisBuild / scalaVersion := "2.12.10"

lazy val root = (project in file("."))
  .aggregate(reactorScala, micrometerTest)
	.settings(
		crossScalaVersions := supportedScalaVersions,
		name := "reactor-scala-extensions-root",
		publish := {},
		publishLocal := {}
	)

lazy val reactorScala = (project in file("reactor-scala"))
  	.settings(
			crossScalaVersions := supportedScalaVersions,
			name := "reactor-scala-extensions",
			libraryDependencies += reactorCore,
			libraryDependencies += scalaCollCompat,
			libraryDependencies += mockitoScala,
			libraryDependencies += scalaTest,
			libraryDependencies += reactorTest,
			libraryDependencies += mockitoInline,
			// Scala Compiler Options
			scalacOptions ++= Seq(
			"-target:jvm-1.8"
			),
			// Prevent test being executed in parallel as it may failed for the Scheduler and Virtual Scheduler
			Test / parallelExecution := false,
			// Scaladoc compiler options
			Compile / doc / scalacOptions ++= Seq(
				"-no-link-warnings"
			),
			autoAPIMappings := true,
			// SCoverage
			//coverageEnabled := true
			coverageMinimum := 80.74,
			coverageFailOnMinimum := true
		)

lazy val micrometerTest = (project in file("micrometer-test"))
  .dependsOn(reactorScala)
  .settings(
		crossScalaVersions := supportedScalaVersions,
		name := "micrometer-test",
		publish := {},
		publishLocal := {},
		libraryDependencies += scalaTest,
		libraryDependencies += micrometer
	)