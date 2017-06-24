import CommonSettings._
import Dependencies._
import ScoverageSbtPlugin.ScoverageKeys._

name := """cc-Knolshare"""

version := "1.0"

scalaVersion := scala

val scoverageSettings = Seq(
  coverageExcludedPackages := "router;" +
  "com\\.knoldus\\.views.*;" +
  "<empty>;controllers.javascript;controllers\\..*Reverse.*;" +
  "com\\.knoldus\\.models\\..*;" +
  "com\\.knoldus\\.utils\\..*;",
  coverageExcludedFiles := "",
  coverageMinimum := 80,
  coverageFailOnMinimum := true
)

lazy val root = (
  project.in(file("."))
  aggregate(common, persistence, api, web, processing)
)

lazy val common = (
  baseProject("common")
  settings(libraryDependencies ++= playDependencies)
  settings(scoverageSettings: _*)
)

lazy val persistence = (
  baseProject("persistence")
    settings(libraryDependencies ++=persistenceDependencies)
    settings(scoverageSettings: _*)
) dependsOn common

lazy val web = (
  playProject("web")
    settings(libraryDependencies ++= webDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn (common,notification)

lazy val api = (
  playProject("api")
    settings(libraryDependencies ++= playDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn common

lazy val processing = (
  playProject("processing")
    settings(libraryDependencies ++= processingDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
) dependsOn common

lazy val notification = (
  playProject("notification")
    settings(libraryDependencies ++= notificationDependencies)
    settings(routesGenerator := InjectedRoutesGenerator)
    settings(scoverageSettings: _*)
  ) dependsOn persistence

