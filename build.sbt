name := "scala-netty"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
        "io.netty" % "netty-all" % "4.1.25.Final",
        "org.slf4j" % "slf4j-log4j12" % "1.7.25",
        "org.apache.commons" % "commons-collections4" % "4.3",
        "org.springframework" % "spring-context" % "4.3.22.RELEASE"
)

val resolvers = "jcenterRepo" at "http://jcenter.bintray.com/"
