package models

import org.openrdf.repository.Repository
import org.openrdf.repository.sparql.config._
import org.openrdf.query.TupleQueryResult
import org.openrdf.query.QueryLanguage
import org.openrdf.query.impl.TupleQueryResultBuilder
import java.io.Closeable

class SparqlSelectRunner(repo: Repository) extends Closeable{

	def evaluate(query: String): Either[String,SparqlTextTable] = {
		val conn = repo.getConnection
		try{
			//val str = new SPARQLResultsTSVWriter(Console.out)
			val handler = new TupleQueryResultBuilder()
			conn.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate(handler)
			Right(new SparqlTextTable(handler.getQueryResult))
		}
		catch{
		  	case e: Exception => Left(e.getMessage)
		}
		finally{conn.close()}
	}

	override def close(){
		repo.shutDown()
	}

}

object SparqlSelectRunner{

	def apply(): SparqlSelectRunner = apply("http://live.dbpedia.org/sparql")

	def apply(endpointUrl: String): SparqlSelectRunner = {
		val factory = new SPARQLRepositoryFactory
		val conf = new SPARQLRepositoryConfig(endpointUrl)
		new SparqlSelectRunner(factory.getRepository(conf))
	}

}
