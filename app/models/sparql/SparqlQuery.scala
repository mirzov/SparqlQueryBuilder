package models.sparql

class SparqlQuery(val triples: Seq[QueryTriple]) {

	override def toString = "SELECT * WHERE{\n" + triples.mkString("\n") + "\n}"
	
}