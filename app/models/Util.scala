package models

object Util {

	def decorateIfUrl(url: String): scala.xml.Node = {
		if(isValidUrl(url)){
			val query = "select distinct * where{<" + url + "> ?property ?value }"
			<a href={"/?query=" + java.net.URLEncoder.encode(query, "UTF-8")}>{url}</a>
		}else <span>{url}</span>
	}
  
	def isValidUrl(url:String): Boolean = try{
		new java.net.URL(url)
		true
	}catch{
	  case _ => false
	}
	
}