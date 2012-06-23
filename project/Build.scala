import sbt._
import Keys._
import PlayProject._
 
object ApplicationBuild extends Build {
 
  val appName         = "SparqlQueryBuilder"
  val appVersion      = "1.0"
 
  val appDependencies = Seq(
//		"org.openrdf.sesame" % "sesame-repository-sail" % "2.6.5" withJavadoc(),
//		"org.openrdf.sesame" % "sesame-sail-memory" % "2.6.5" withJavadoc(),
		"org.scalatest" %% "scalatest" % "1.8" % "test",
		"org.openrdf.sesame" % "sesame-repository-sparql" % "2.6.5" withJavadoc(),
		"org.openrdf.sesame" % "sesame-queryresultio-text" % "2.6.5" withJavadoc(),
		//"org.slf4j" % "slf4j-simple" % "1.6.1",
		"commons-logging" % "commons-logging" % "1.1.1"
  )
  
  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA)
		.settings(defaultScalaSettings :_*)
		.settings(
		    testOptions in Test := Nil,
	  		resolvers += "Aduna" at "http://repo.aduna-software.org/maven2/releases/"
	  )
 
}
