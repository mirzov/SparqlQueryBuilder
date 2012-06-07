package controllers

import play.api.mvc._
import scala.collection.JavaConversions._
import models._
import models.onto.Ontology
import play.api.templates.Html
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
//import play.api.templates.Html

object Application extends Controller {


	def index = Action { request =>
	    val qProfileId = QueryProfiles.default.id
		request.queryString.get("query").map(_.toList) match{
		  	case Some(query :: _) => Ok(views.html.index(qProfileId, query))
		  	case _ => Ok(views.html.index(qProfileId))
		}
		

	}
	
	def sparql = Action{ request =>
		request.queryString.get("query").map(_.toList) match{
		  	case Some(query :: _) => 
		  	  SparqlSelectRunner().evaluate(query) match{
		  	    case Left(errorMsg) => Ok(Html(errorMsg))
		  	    case Right(reply) => Ok(views.html.sparql(reply))
		  	  }
		  	case _ => Ok(Html("No query was provided!"))
		}
	}

	def qprofile = Action { request =>
	  	val ontoId = request.queryString.get("id").map(_.toList) match{
	  	  	case Some(qpId :: _) => qpId
	  	  	case _ => QueryProfiles.default.id
	  	}
	  	val onto = Ontology(ontoId).getOrElse(Ontology(QueryProfiles.default.id).get)
		Ok(views.html.qprofile(onto))
	}
	
}
