import CommonSettings._
import Dependencies._
import ScoverageSbtPlugin.ScoverageKeys._

name := """playing-microservices"""

version := "1.0"

scalaVersion := scala

val scoverageSettings = Seq(
  coverageExcludedPackages := "<empty>;controllers.javascript;views.*;router",
  coverageExcludedFiles := "",
  coverageMinimum := 80,
  coverageFailOnMinimum := true
)

lazy val root = (
  project.in(file("."))
  aggregate(common, persistence, api, web, processing)
)

lazy val common = (
  BaseProject("common")
  settings(libraryDependencies ++= playDependencies)
  settings(scoverageSettings: _*)
)

lazy val persistence = (
  BaseProject("persistence")
    settings(libraryDependencies ++= persistenceDependencies)
    settings(scoverageSettings: _*)
) dependsOn(common)

lazy val web = (
  PlayProject("web")
    settings(libraryDependencies ++= webDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn(common)

lazy val api = (
  PlayProject("api")
    settings(libraryDependencies ++= playDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn(common)

lazy val processing = (
  PlayProject("processing")
    settings(libraryDependencies ++= playDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn(common)

