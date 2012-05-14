package models

trait TextTableRow extends IndexedSeq[String] {
  
	def apply(colName: String): String
	
}

trait TextTable {

	def columnNames: Seq[String]
	def rows: Seq[TextTableRow]
	
}

