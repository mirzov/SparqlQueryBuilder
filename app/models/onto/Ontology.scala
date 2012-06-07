package models.onto

import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.Set
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model._

object Ontology{
	def apply(ontId: String): Option[Ontology] = {
		val istream = getClass.getClassLoader.getResourceAsStream("public/queryprofiles/" + ontId + ".owl")
		if(istream == null) None
		else {
			try{
				val owlManager = OWLManager.createOWLOntologyManager
				val ontology = owlManager.loadOntologyFromOntologyDocument(istream);
				Some(new Ontology(ontology))
			}catch{
			  	case _ => None
			}
		}
	}
	
	private def contains(clsExpr: OWLClassExpression, cls: OWLClass): Boolean = 
		clsExpr.asDisjunctSet.exists(_.compareTo(cls) == 0)
	
}

class Ontology private (val ontology: OWLOntology) {

	import Implicits._
	import Ontology._
	
	private implicit val onto = ontology
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
	
	def getDataTypeLabel(prop: OWLDataProperty): String = {
		val dtUri = prop.getRanges(ontology).head.asOWLDatatype.getIRI.toString
		val xsd = "http://www.w3.org/2001/XMLSchema#"
		if(dtUri.startsWith(xsd)) "xsd:" + dtUri.stripPrefix(xsd) else dtUri
	}
}