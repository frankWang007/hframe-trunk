package com.ucfgroup.client.beetle.bean.getassetchangeresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import com.hframework.common.util.message.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * generated by hframework on 2016-04-22.
 */
public class Data   {

	@JsonProperty("count")
	private String count;
	@JsonProperty("list")
	private List<ListBean> listList;
	@XStreamOmitField
	private boolean converted;

    public Data() {
    }
 	
	public Data convert()  throws Exception{
			if(!converted) {
			   String beforeInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(beforeInfo);
			   converted = true;
			   String afterInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(afterInfo);
			}
			return this;
	}

  
 	 	 
     public String getCount(){
     	return count;
     }

     public void setCount(String count){
     	this.count = count;
     }
	 	 	 
     public List<ListBean> getListList(){
     	return listList;
     }

     public void setListList(List<ListBean> listList){
     	this.listList = listList;
     }
	 	 
}