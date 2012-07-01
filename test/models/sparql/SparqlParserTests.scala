package models.sparql

import org.scalatest.FunSpec

class SparqlParserTests extends FunSpec {
	import SparqlParser._
	
	def assertParsingSuccess[T](parser : => Parser[T], input: String) = 
		assert(parseAll(parser, input).successful)
		
	def assertParsingFailure[T](parser : => Parser[T], input: String) = 
		assert(!parseAll(parser, input).successful)
		
	describe("ConstResource parser"){
	  
		def yes(input: String) = assertParsingSuccess(constResource, input)
		def no(input: String) = assertParsingFailure(constResource, input)
	
		val u1 = "<http://www.bbb.mmm>"
		val u2 = "http://www.bbb.mmm>"
		val u3 = "<http://www.bbb.mmm"
		val u4 = "<aaa>"
		val u5 = "<www.bbb.mmm>"
		val u6 = "<http://w ww.bbb.mmm>"
		val u7 = "<http://www.bbb.m>mm>"
		  
		it("should successfully parse " + u1){yes(u1)}
		it("should fail on " + u2){no(u2)}
		it("should fail on " + u3){no(u3)}
		it("should fail on " + u4){no(u4)}
		it("should fail on " + u5){no(u5)}
		it("should fail on " + u6){no(u6)}
		it("should fail on " + u7){no(u7)}
	}
	
	describe("Variable parser"){
	  
		def yes(input: String) = assertParsingSuccess(variable, input)
		def no(input: String) = assertParsingFailure(variable, input)
		
		val v1 = "?v"
		val v2 = "bebe"
		val v3 = "?_v123FJr"
		val v4 = "?5463"
		val v5 = "? v"
		  
		it("should successfully parse " + v1){yes(v1)}
		it("should fail on " + v2){no(v2)}
		it("should successfully parse " + v3){yes(v3)}
		it("should successfully parse " + v4){yes(v4)}
		it("should fail on " + v5){no(v5)}
	}
	
	describe("ConstLiteral parsers"){
	  
		describe("plainLiteral"){
		  
			def yes(input: String) = assertParsingSuccess(plainLiteral, input)
			def no(input: String) = assertParsingFailure(plainLiteral, input)
			
			val s1 = "'string'"
			val s2 = "'st\\'ri\\'ng'"
			val s3 = "''"
			val s4 = "'\\''"
			  
			it("should successfully parse " + s1){yes(s1)}
			it("should successfully parse " + s2){yes(s2)}
			
			it("should extract \"st'ri'ng\" from " + s2){
				val parseRes = parseAll(plainLiteral, s2)
				assert(parseRes.successful)
				val strRes = parseRes.get.lit.stringValue
				assert(strRes === "st'ri'ng")
			}
			
			it("should successfully parse " + s3){yes(s3)}
			it("should successfully parse " + s4){yes(s4)}
		}
		
		describe("langLiteral"){
		  
			def yes(input: String) = assertParsingSuccess(langLiteral, input)
			def no(input: String) = assertParsingFailure(langLiteral, input)
			
			val s1 = "'string'@en"
			val s2 = "'string'"
			val s3 = "'bebe'@ru"
			  
			it("should successfully parse " + s1){yes(s1)}
			it("should fail on " + s2){no(s2)}
			it("should successfully parse " + s3){yes(s3)}
			
			it("should extract an @en literal 'string' from " + s1){
				val parseRes = parseAll(langLiteral, s1)
				assert(parseRes.successful)
				val res = parseRes.get.lit
				assert(res.getLabel === "string")
				assert(res.getLanguage === "en")
			}
		}
		
		describe("typedLiteral"){
		  
			def yes(input: String) = assertParsingSuccess(typedLiteral, input)
			def no(input: String) = assertParsingFailure(typedLiteral, input)
			
			val s1 = "'string'^^<http://www.bbb.mmm>"
			val s2 = "'string'^^<www.bbb.mmm>"
			  
			it("should successfully parse " + s1){yes(s1)}
			it("should fail on " + s2){no(s2)}
			
			it("should correctly parse " + s1){
				val parseRes = parseAll(typedLiteral, s1)
				assert(parseRes.successful)
				val res = parseRes.get.lit
				assert(res.getLabel === "string")
				assert(res.getDatatype.stringValue === "http://www.bbb.mmm")
			}
		}
	}
	
	describe("queryTriple parser"){
	  
		def yes(input: String) = assertParsingSuccess(queryTriple, input)
		def no(input: String) = assertParsingFailure(queryTriple, input)
		
		val s1 = "?var a <http://www.bbb.mmm> ."
		val s2 = "<http://www.bbb.mmm> ?pred ?o." //no space before comma
		val s3 = "<http://www.bbb.mmm> ?pred ?o ." 
		val s4 = "<http://www.bbb.mmm> ?pred 'bebe'@ru ."
		val s5 = "?v <http://www.bbb.mmm> ?o ." 
		  
		it("should successfully parse " + s1){yes(s1)}
		it("should fail on " + s2){no(s2)}
		it("should successfully parse " + s3){yes(s3)}
		it("should successfully parse " + s4){yes(s4)}
		it("should successfully parse " + s5){yes(s5)}
		
		it("should correctly parse " + s1){
			val parseRes = parseAll(queryTriple, s1)
			assert(parseRes.successful)
			parseRes.get match{
			  	case QueryTriple(s: Variable, p: HasType.type, o: ConstResource) =>
			  		assert(s.name === "var")
			  		assert(o.uri.stringValue === "http://www.bbb.mmm")
			  	case _ => fail("did not get the expected query triple")
			}
		}
	}
}