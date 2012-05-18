package models.onto

import scala.collection.JavaConversions.asScalaSet

import org.openrdf.repository.Repository
import org.openrdf.repository.RepositoryConnection
import org.semanticweb.owlapi.model.OWLAnnotation
import org.semanticweb.owlapi.model.OWLEntity
import org.semanticweb.owlapi.model.OWLLiteral
import org.semanticweb.owlapi.model.OWLOntology

object Implicits {

	implicit def repo2SesameRepoWithConnectionHandling(repo: Repository) = 
		new SesameRepoWithConnectionHandling(repo)
	
	implicit def owlEntity2owlEntityWithAnnotations(owlEntity: OWLEntity)(implicit owlOnt: OWLOntology) = 
		new OWLEntityWithAnnotations(owlEntity, owlOnt)
  

	class SesameRepoWithConnectionHandling(inner: Repository){
	  
		def access(body: RepositoryConnection => Unit){
			val conn = inner.getConnection
			try{
				body(conn)
			}finally{
				conn.close()
			}
		}
	  
	}
	
	class OWLEntityWithAnnotations(owlEntity: OWLEntity, owlOntology: OWLOntology){
		
		private def getAnnoValue(selector: OWLAnnotation => Boolean): String = 
			owlEntity.getAnnotations(owlOntology)
			.filter(selector)
			.headOption match{
				case Some(anno) => anno.getValue.asInstanceOf[OWLLiteral].getLiteral
				case None => owlEntity.getIRI.toString 
			} 
		
		def label: String = getAnnoValue(_.getProperty.isLabel)
		def comment: String = getAnnoValue(_.getProperty.isComment)
	}

}
