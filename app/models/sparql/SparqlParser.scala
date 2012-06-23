package models.sparql

import scala.util.parsing.combinator.RegexParsers
import org.openrdf.model.URI
import org.openrdf.model.impl.ValueFactoryImpl

object SparqlParser extends RegexParsers{

	private val factory = new ValueFactoryImpl()
  
	private def makeConstResource(uriCand: String): Option[ConstResource] = {
		try{
			Some(ConstResource(factory.createURI(uriCand)))
		}catch{
		  	case _ => None
		}
	}
	
	override def skipWhitespace = false
	
	def variable: Parser[Variable] = "?" ~> """\w+""".r ^^ {v => Variable(v)}
  
	def constResource: Parser[ConstResource] = "<" ~> """[^(\s|>)]+""".r <~ ">" ^? Function.unlift(makeConstResource)
	
	def hasType: Parser[HasType] = "a" ^^ {_ => HasType()}
	
	def stringPartUpToQuotEscape: Parser[String] = """.*?\\'""".r
	def stringEndingWithQuot: Parser[String] = ".*?'".r
	
	def plainLiteral: Parser[ConstLiteral] = "'" ~> rep(stringPartUpToQuotEscape) ~ stringEndingWithQuot ^^ {
	  	case list ~ rest =>
	  	  	println(list + " , " + rest)
	  	  	val str = list.map(s => s.replace("\\'", "'")).mkString + rest.stripSuffix("'")
	  	  	println(str)
	  	  	ConstLiteral(factory.createLiteral(str))
	}
}