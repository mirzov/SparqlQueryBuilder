package models

import org.openrdf.query.TupleQueryResult
import scala.collection.JavaConversions._
import org.openrdf.query.BindingSet
import org.openrdf.model._


class SparqlTextTable(qRes: TupleQueryResult) extends TextTable {
	
	override val columnNames: Seq[String] = qRes.getBindingNames.toList
	
	override val rows: Seq[TextTableRow] = getResStream(qRes)
	
	private val colIndexLookup = columnNames.zipWithIndex.toMap
	private val nOfCols = columnNames.length
	
	private def getResStream(tqr: TupleQueryResult): Stream[TextTableRow] = {
		if(tqr.hasNext){
			val next = tqr.next()
			val row = columnNames.map(valueToStr(next, _)).toArray
			Stream.cons(new SparqlTableRow(row), getResStream(tqr))
		}else {println("closing qRes...");tqr.close();println("closed"); Stream.Empty}
	}
	
	def valueToStr(bindSet: BindingSet, vName: String): String = {
		val binding = bindSet.getBinding(vName)
		if(binding == null) ""
		else binding.getValue match {
			case n if(null == n) => ""
			case uri: URI => uri.stringValue
			case lit: Literal => {
				val dataType = lit.getDatatype
				if(dataType == null) lit.stringValue
				else lit.stringValue + "^^" + dataType.stringValue
			}
			case blank: BNode => blank.getID
		}
	}
  
	private class SparqlTableRow(row: Array[String]) extends TextTableRow{
		override val length = row.length
		//assert(row.length == nOfCols, "DataTable must have the same number of columns in every row!")
		def apply(i: Int) = row(i)
		def apply(colName: String) = row(colIndexLookup(colName))
	}
}
