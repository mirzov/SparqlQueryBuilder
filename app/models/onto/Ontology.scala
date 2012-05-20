package models.onto

import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.Set
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model._

object Ontology{
	def apply(ontId: String): Ontology = {
		val istream = getClass.getClassLoader.getResourceAsStream("public/queryprofiles/" + ontId + ".owl")
		val owlManager = OWLManager.createOWLOntologyManager
		val ontology = owlManager.loadOntologyFromOntologyDocument(istream);
		new Ontology()(ontology)
	}
	
	private def contains(clsExpr: OWLClassExpression, cls: OWLClass): Boolean = 
		clsExpr.asDisjunctSet.exists(_.compareTo(cls) == 0)
	
}

class Ontology private (implicit val ontology: OWLOntology) {

	import Implicits._
	import Ontology._
	
	val classes: Seq[OWLClass] = ontology.getClassesInSignature.toSeq

	val dataProps: Seq[OWLDataProperty] = ontology.getDataPropertiesInSignature.toSeq
	val objProps: Seq[OWLObjectProperty] = ontology.getObjectPropertiesInSignature.toSeq
	
	def getLabel(owlEntity: OWLEntity): String = owlEntity.label
	
	def getClassesInDomainOf(prop: OWLDataProperty): Seq[OWLClass] = classes.filter{cls =>
	  	prop.getDomains(ontology).forall(contains(_, cls))
	}
	
	def getClassesInDomainOf(prop: OWLObjectProperty): Seq[OWLClass] = classes.filter{cls =>
	  	prop.getDomains(ontology).forall(contains(_, cls))
	}
	
	def getClassesInRangeOf(prop: OWLObjectProperty): Seq[OWLClass] = classes.filter{cls =>
	  	prop.getRanges(ontology).forall(contains(_, cls))
	}
	
	def getDataType(prop: OWLDataProperty): OWLDatatype = {
		prop.getRanges(ontology).head.asOWLDatatype
	}
}