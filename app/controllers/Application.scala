package controllers

import play.api.mvc._
import models._
//import play.api.templates.Html

object Application extends Controller {


	def index = Action {

		Ok(views.html.index())

	}
	
	def sparql = Action{ request =>
		request.queryString("query").headOption match{
		  	case None => Ok(views.html.error("No query was provided!"))
		  	case Some(query) => 
		  	  SparqlSelectRunner().evaluate(query) match{
		  	    case Left(errorMsg) => Ok(views.html.error(errorMsg))
		  	    case Right(reply) => Ok(views.html.sparql(reply))
		  	  }
		}
	}

}
