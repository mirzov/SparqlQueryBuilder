package controllers

import play.api.mvc._
import models._
import models.onto.Ontology
import play.api.templates.Html
//import play.api.templates.Html

object Application extends Controller {


	def index = Action { request =>
	  	val onto = Ontology("movies")
		request.queryString.get("query").map(_.toList) match{
		  	case Some(query :: _) => Ok(views.html.index(onto, query))
		  	case _ => Ok(views.html.index(onto))
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

}
