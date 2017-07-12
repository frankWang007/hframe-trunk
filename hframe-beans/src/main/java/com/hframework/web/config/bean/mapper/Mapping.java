package com.hframework.web.config.bean.mapper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * generated by hframework on 2016.
 */@XStreamAlias("mapping")
public class Mapping   {

	@XStreamImplicit
    @XStreamAlias("mapping")
	private List<Mapping> mappingList;
	@XStreamAsAttribute
    @XStreamAlias("id")
	private String id;
	@XStreamAsAttribute
    @XStreamAlias("value")
	private String value;
	@XStreamAsAttribute
    @XStreamAlias("type")
	private String type;

    public Mapping() {
    }
   
 	 	 
     public List<Mapping> getMappingList(){
     	return mappingList == null ? new ArrayList<Mapping>() : mappingList;
     }

     public void setMappingList(List<Mapping> mappingList){
     	this.mappingList = mappingList;
     }
	 	 	 
     public String getId(){
     	return id;
     }

     public void setId(String id){
     	this.id = id;
     }
	 	 	 
     public String getValue(){
     	return value;
     }

     public void setValue(String value){
     	this.value = value;
     }
	 	 	 
     public String getType(){
     	return type;
     }

     public void setType(String type){
     	this.type = type;
     }
	 
}