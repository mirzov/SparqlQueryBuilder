package models.sparql

import org.openrdf.model.URI
import org.openrdf.model.Literal

sealed trait QueryNode
trait QuerySubject extends QueryNode
trait QueryPredicate extends QueryNode
trait QueryObject extends QueryNode

case class Variable(name: String) extends QuerySubject with QueryPredicate with QueryObject{
	override def toString = "?" + name
}

case class AnonNode(id: Int) extends QuerySubject with QueryObject{
	assert(id >= 0, "Anonymous node id must not be negative!")
	override def toString = ":_" + id
}

case class ConstResource(uri: URI) extends QuerySubject with QueryPredicate with QueryObject{
	override def toString = "<" + uri.stringValue + ">"
}

case class ConstLiteral(lit: Literal) extends QueryObject{
  
	def typeOrTag: String = {
		if(lit.getLanguage == null) {
			if(lit.getDatatype == null) "" else "^^<" + lit.getDatatype.stringValue + ">"
		}else "@" + lit.getLanguage
	}
	override def toString = "'" + lit.getLabel + "'" + typeOrTag
}

case object HasType extends QueryPredicate{
	override def toString = "a"
}
