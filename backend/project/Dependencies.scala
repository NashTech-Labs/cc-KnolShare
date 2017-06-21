import sbt._

object Dependencies {

  val scala = "2.11.7"

  val resolvers = DefaultOptions.resolvers(snapshot = true) ++ Seq(
    "scalaz-releases" at "http://dl.bintray.com/scalaz/releases"
  )
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.3"
  val slick = "com.typesafe.slick" %% "slick" % "3.0.0"
  val postgresql = "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
  val h2database = "com.h2database" % "h2" % "1.4.187" % "test"
  val kafkaclients = "org.apache.kafka" % "kafka-clients" % "0.10.2.0"
  val twitter4j = "org.twitter4j" % "twitter4j-stream" % "4.0.6"
  val kafkastreams = "org.apache.kafka" % "kafka-streams" % "0.10.2.0"
  val playDependencies: Seq[ModuleID] = Seq(
    PlayFramework.jdbc,
    PlayFramework.cache,
    PlayFramework.ws,
    PlayFramework.json,
    PlayFramework.specs2
  )
  val slackDependencies = Seq(
    SlackRunner.slackClient
  )
  val scalaTestDependencies = Seq(
    ScalaTest.mockitoTest,
    ScalaTest.scalaTest
  )
  val mailDependencies = Seq(
    JavaMailer.mail,
    JavaMailer.mockedMail
  )
  val webjarsDependencies: Seq[ModuleID] = playDependencies ++ Seq(
    WebJars.webjarsplay,
    WebJars.bootstrap,
    WebJars.requirejs,
    WebJars.jquery,
    WebJars.fontawesome
  )
  val notificationDependencies: Seq[ModuleID] = playDependencies ++ mailDependencies ++
                                                slackDependencies ++ scalaTestDependencies
  val webDependencies: Seq[ModuleID] = playDependencies ++ webjarsDependencies
  val persistenceDependencies: Seq[ModuleID] = playDependencies ++ Seq(
    postgresql,
    slick,
    h2database
  )
  val processingDependencies: Seq[ModuleID] = playDependencies ++ Seq(
    kafkaclients,
    twitter4j,
    kafkastreams
  )
  object PlayFramework {
    val version = play.core.PlayVersion.current

    val jdbc = "com.typesafe.play" %% "play-jdbc" % version
    val cache = "com.typesafe.play" %% "play-cache" % version
    val ws = "com.typesafe.play" %% "play-ws" % version
    val json = "com.typesafe.play" %% "play-json" % version
    val specs2 = "com.typesafe.play" %% "play-specs2" % version % "test"
  }

  object ScalaTest {
    val mockitoTest = "org.mockito" % "mockito-core" % "1.10.19"
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6"
  }

  object JavaMailer {
    val mail = "javax.mail" % "mail" % "1.4"
    val mockedMail = "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"

  }


  object SlackRunner {
    val slackClient = "com.github.gilbertw1" %% "slack-scala-client" % "0.1.8"
  }

  object WebJars {
    val webjarsplay = "org.webjars" %% "webjars-play" % "2.4.0-1"
    val bootstrap = "org.webjars" % "bootstrap" % "3.3.4"
    val requirejs = "org.webjars" % "requirejs" % "2.1.18"
    val jquery = "org.webjars" % "jquery" % "2.1.4"
    val fontawesome = "org.webjars" % "font-awesome" % "4.3.0-2"
  }

  object Dependencies {

    val scala = "2.11.7"

    val resolvers = DefaultOptions.resolvers(snapshot = true) ++ Seq(
      "scalaz-releases" at "http://dl.bintray.com/scalaz/releases"
    )
    val logback = "ch.qos.logback" % "logback-classic" % "1.1.3"
    val slick = "com.typesafe.slick" %% "slick" % "3.0.0"
    val postgresql = "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
    val h2database = "com.h2database" % "h2" % "1.4.187" % "test"
    val scalatest = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
    val kafkaclients = "org.apache.kafka" % "kafka-clients" % "0.10.2.0"
    val twitter4j = "org.twitter4j" % "twitter4j-stream" % "4.0.6"
    val kafkastreams = "org.apache.kafka" % "kafka-streams" % "0.10.2.0"
    val playDependencies: Seq[ModuleID] = Seq(
      PlayFramework.jdbc,
      PlayFramework.cache,
      PlayFramework.ws,
      PlayFramework.json,
      PlayFramework.specs2
    )
    val mailDependencies = Seq(
      JavaMailer.mail,
      JavaMailer.mockedMail
    )
    val webjarsDependencies: Seq[ModuleID] = playDependencies ++ Seq(
      WebJars.webjarsplay,
      WebJars.bootstrap,
      WebJars.requirejs,
      WebJars.jquery,
      WebJars.fontawesome
    )
    val notificationDependencies: Seq[ModuleID] = playDependencies ++ mailDependencies
    val webDependencies: Seq[ModuleID] = playDependencies ++ webjarsDependencies
    val persistenceDependencies: Seq[ModuleID] = playDependencies ++ Seq(
      postgresql,
      slick,
      h2database
    )
    val processingDependencies: Seq[ModuleID] = playDependencies ++ Seq(
      kafkaclients,
      twitter4j,
      kafkastreams
    )

    object PlayFramework {
      val version = play.core.PlayVersion.current

      val jdbc = "com.typesafe.play" %% "play-jdbc" % version
      val cache = "com.typesafe.play" %% "play-cache" % version
      val ws = "com.typesafe.play" %% "play-ws" % version
      val json = "com.typesafe.play" %% "play-json" % version
      val specs2 = "com.typesafe.play" %% "play-specs2" % version % "test"
    }

    object JavaMailer {
      val mail = "javax.mail" % "mail" % "1.4"
      val mockedMail = "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"

    }

    object WebJars {
      val webjarsplay = "org.webjars" %% "webjars-play" % "2.4.0-1"
      val bootstrap = "org.webjars" % "bootstrap" % "3.3.4"
      val requirejs = "org.webjars" % "requirejs" % "2.1.18"
      val jquery = "org.webjars" % "jquery" % "2.1.4"
      val fontawesome = "org.webjars" % "font-awesome" % "4.3.0-2"
    }

  }

}
