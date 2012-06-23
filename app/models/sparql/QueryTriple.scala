package models.sparql


case class QueryTriple(subj: QuerySubject, pred: QueryPredicate, obj: QueryObject) {

	override def toString = "%s %s %s .".format(subj, pred, obj)
	
	def nodes: Seq[QueryNode] = Array(subj, pred, obj)
	
	def variables: Seq[Variable] = nodes.collect{case v:Variable => v}
}


