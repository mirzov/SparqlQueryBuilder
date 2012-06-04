package models

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

case class QueryProfile(id: String, description: String) {

}

object QueryProfiles{
  
	val all : Seq[QueryProfile] = {
		val conf = ConfigFactory.load()
		conf.getObjectList("queryProfiles").map{qpConf =>
			QueryProfile(qpConf.get("id").unwrapped.toString, qpConf.get("description").unwrapped.toString)
		}
	}
	
	def default = all.head
}