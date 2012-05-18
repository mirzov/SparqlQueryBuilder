package controllers

import play.api.mvc._
import models._
import play.api.templates.Html
//import play.api.templates.Html

object Application extends Controller {


	def index = Action {

		Ok(views.html.index())

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
