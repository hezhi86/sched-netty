name := "scala-netty"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
        "io.netty" % "netty-all" % "4.1.36.Final",
        "org.slf4j" % "slf4j-log4j12" % "1.7.26",
        "org.apache.commons" % "commons-collections4" % "4.3",
        "org.springframework" % "spring-context" % "4.3.22.RELEASE",
        "io.projectreactor.netty" % "reactor-netty" % "0.8.9.RELEASE",
        "com.orbitz.consul" % "consul-client" % "1.3.6",
)

val resolvers = "jcenterRepo" at "http://jcenter.bintray.com/"
