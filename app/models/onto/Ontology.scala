package models.onto

import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.Set
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLProperty
import org.semanticweb.owlapi.model.OWLEntity
import org.semanticweb.owlapi.model.OWLDataProperty
import org.semanticweb.owlapi.model.OWLObjectProperty

object Ontology {

	import Implicits._

	private val istream = getClass.getClassLoader.getResourceAsStream("public/queryprofiles/movies.owl")
	
	private val owlManager = OWLManager.createOWLOntologyManager
  
	implicit val ontology = owlManager.loadOntologyFromOntologyDocument(istream);
	
	val classes: Seq[OWLClass] = ontology.getClassesInSignature.toSeq

	val dataProps: Seq[OWLDataProperty] = ontology.getDataPropertiesInSignature.toSeq
	val objProps: Seq[OWLObjectProperty] = ontology.getObjectPropertiesInSignature.toSeq
	
	def getLabel(owlEntity: OWLEntity): String = owlEntity.label
	
	def getClassesInDomainOf(prop: OWLDataProperty): Seq[OWLClass] = classes.filter{cls =>
		prop.getDomains(ontology).forall(_.containsConjunct(cls))
	}
	
	def getClassesInDomainOf(prop: OWLObjectProperty): Seq[OWLClass] = classes.filter{cls =>
		prop.getDomains(ontology).forall(_.containsConjunct(cls))
	}
}