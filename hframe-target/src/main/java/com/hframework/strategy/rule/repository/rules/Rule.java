package com.hframework.strategy.rule.repository.rules;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2017.
 */@XStreamAlias("rule")
public class Rule   {

	@XStreamImplicit/n    @XStreamAlias("description")
	private List<String> descriptionList;
	@XStreamImplicit/n    @XStreamAlias("expression")
	private List<String> expressionList;
	@XStreamAlias("relateRules")
	private RelateRules relateRules;
	@XStreamAsAttribute/n    @XStreamAlias("code")
	private String code;
	@XStreamAsAttribute/n    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute/n    @XStreamAlias("version")
	private String version;
	@XStreamAsAttribute/n    @XStreamAlias("returnType")
	private String returnType;

    public Rule() {
    }
   
 	 	 
     public List<String> getDescriptionList(){
     	return descriptionList;
     }

     public void setDescriptionList(List<String> descriptionList){
     	this.descriptionList = descriptionList;
     }
	 	 	 
     public List<String> getExpressionList(){
     	return expressionList;
     }

     public void setExpressionList(List<String> expressionList){
     	this.expressionList = expressionList;
     }
	 	 	 
     public RelateRules getRelateRules(){
     	return relateRules;
     }

     public void setRelateRules(RelateRules relateRules){
     	this.relateRules = relateRules;
     }
	 	 	 
     public String getCode(){
     	return code;
     }

     public void setCode(String code){
     	this.code = code;
     }
	 	 	 
     public String getName(){
     	return name;
     }

     public void setName(String name){
     	this.name = name;
     }
	 	 	 
     public String getVersion(){
     	return version;
     }

     public void setVersion(String version){
     	this.version = version;
     }
	 	 	 
     public String getReturnType(){
     	return returnType;
     }

     public void setReturnType(String returnType){
     	this.returnType = returnType;
     }
	 
}