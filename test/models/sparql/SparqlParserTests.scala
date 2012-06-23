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
	
	describe("stringPartUpToQuotEscape parser"){
	  
		def yes(input: String) = assertParsingSuccess(stringPartUpToQuotEscape, input)
		def no(input: String) = assertParsingFailure(stringPartUpToQuotEscape, input)
		
		val s1 = "'string'"
		val s2 = "st\\'"
		val s3 = "st\\'ring\\'"
		  
		it("should fail on " + s1){no(s1)}
		it("should successfully parse " + s2){yes(s2)}
		it("when rep'ed, should match " + s3){
			val parseRes = parseAll(rep(stringPartUpToQuotEscape), s3)
			assert(parseRes.successful)
			assert(parseRes.get === List("st\\'", "ring\\'"))
		}
	}
	
	describe("ConstLiteral parser, plainLiteral version"){
	  
		def yes(input: String) = assertParsingSuccess(plainLiteral, input)
		def no(input: String) = assertParsingFailure(plainLiteral, input)
		
		val s1 = "'string'"
		val s2 = "'st\\'ri\\'ng'"
		  
		it("should successfully parse " + s1){yes(s1)}
		it("should successfully parse " + s2){yes(s2)}
		//it("should fail on " + s2){no(s2)}
		
		it("should extract \"st'ri'ng\" from " + s2){
			val parseRes = parseAll(plainLiteral, s2)
			assert(parseRes.successful)
			val strRes = parseRes.get.lit.stringValue
			assert(strRes === "st'ri'ng")
		}
	}
}