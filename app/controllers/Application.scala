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
		  	  val reply = SparqlSelectRunner().evaluate(query)
		  	  Ok(views.html.sparql(reply))
		}
	}

}
