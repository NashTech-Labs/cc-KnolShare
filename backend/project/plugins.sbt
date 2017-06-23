resolvers ++= DefaultOptions.resolvers(snapshot = true)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.0")

//Idea plugin
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")

// Scala Style Plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

// scoverage Plugin
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.1.0")

// sbt-web plugin for gzip compressing web assets
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.8")

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"