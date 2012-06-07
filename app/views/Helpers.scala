package views

import play.api.templates.Html

object Helpers {
	
	def joinWithSep(xmls: Seq[Html], sep: Html): Html = {
		xmls.toList match {
		  case Nil => Html.empty
		  case first :: Nil => first
		  case first :: rest => first + sep + joinWithSep(rest, sep)
		}
	}
  
}