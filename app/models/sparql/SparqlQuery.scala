package models.sparql

case class SparqlQuery(outputVars: Seq[Variable], triples: Seq[QueryTriple]) {

	override def toString = "select " + outputVars.mkString(" ") + " where{\n" + triples.mkString("\n") + "\n}"
	
	val allVars: Seq[Variable] = triples.flatMap(_.variables).distinct
	
	assert(outputVars.diff(allVars).isEmpty, "Variables expected to return from a SPARQL query must be mentioned in the triples.")
	
}