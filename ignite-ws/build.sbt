name := """ignite-ws"""
organization := "taiga.research"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += "org.apache.ignite" % "ignite-core" % "2.1.0"
libraryDependencies += "org.apache.ignite" % "ignite-spring" % "2.1.0"