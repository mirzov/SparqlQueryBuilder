import sbt._
import Keys._
import PlayProject._
 
object ApplicationBuild extends Build {
 
  val appName         = "SparqlQueryBuilder"
  val appVersion      = "1.0"
 
  resolvers += "Aduna" at "http://repo.aduna-software.org/maven2/releases/"

  val appDependencies = Seq(
		"org.openrdf.sesame" % "sesame-repository-sparql" % "2.6.5" withJavadoc(),
		"org.openrdf.sesame" % "sesame-queryresultio-text" % "2.6.5" withJavadoc(),
		//"org.slf4j" % "slf4j-simple" % "1.6.1",
		"commons-logging" % "commons-logging" % "1.1.1"
  )
 
  val main = PlayProject(
    appName, appVersion, appDependencies, mainLang = SCALA
  ) 
 
}
