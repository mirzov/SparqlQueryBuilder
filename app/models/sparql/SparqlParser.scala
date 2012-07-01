package models.sparql

import scala.util.parsing.combinator.RegexParsers
import org.openrdf.model.URI
import org.openrdf.model.impl.ValueFactoryImpl

object SparqlParser extends RegexParsers{

	private val factory = new ValueFactoryImpl()
  
	private def makeUri(uriCand: String): Option[URI] = {
		try{
			Some(factory.createURI(uriCand))
		}catch{
		  	case _ => None
		}
	}
	
	override def skipWhitespace = false
	
	val variable: Parser[Variable] = "?" ~> """\w+""".r ^^ {v => Variable(v)}
  
	val uriRes: Parser[URI] = "<" ~> """[^\s>]+""".r <~ ">"  ^? Function.unlift(makeUri)
	
	val constResource: Parser[ConstResource] = uriRes ^^ {ConstResource(_)}
	
	val hasType: Parser[HasType.type] = "a" ^^ {_ => HasType}
	
	val trivialString: Parser[String] = """[^\\']+""".r
	val escapeSeq: Parser[String] = ".{2}".r ^? {
	  	case "\\\\" => "\\"
	  	case "\\'" => "'"
	  	case "\\\"" => "\""
	  	case "\\r" => "\r"
	  	case "\\t" => "\t"
	  	case "\\n" => "\n"
	  	case "\\b" => "\b"
	  	case "\\f" => "\f"
	}
	
	val plainLiteralStr: Parser[String] = ("'" ~> rep(trivialString | escapeSeq)) <~ "'" ^^ (_.mkString)
	
	val plainLiteral: Parser[ConstLiteral] = plainLiteralStr ^^ {
		s => ConstLiteral(factory.createLiteral(s))
	}
	
	val langLiteral: Parser[ConstLiteral] = (plainLiteralStr <~ "@") ~ "\\w{2}".r ^^{
		case str ~ lang => ConstLiteral(factory.createLiteral(str, lang))
	}
	
	val typedLiteral: Parser[ConstLiteral] = (plainLiteralStr <~ "^^") ~ uriRes ^^ {
	  	case str ~ uri => ConstLiteral(factory.createLiteral(str, uri))
	}
	
	val constLiteral: Parser[ConstLiteral] = typedLiteral | langLiteral | plainLiteral
	
	val ws = whiteSpace
	
	val querySubject: Parser[QuerySubject] = constResource | variable 
	val queryPredicate: Parser[QueryPredicate] = hasType | variable | constResource
	val queryObject: Parser[QueryObject] = variable | constResource | constLiteral
	
	val queryTriple: Parser[QueryTriple] = (querySubject <~ ws) ~ (queryPredicate <~ ws) ~ (queryObject <~ ws <~ ".") ^^{
	  	case qs ~ qp ~ qo => QueryTriple(qs, qp, qo)
	}
	
	
}