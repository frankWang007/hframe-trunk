package com.ucfgroup.client.beetle.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hframework.common.util.message.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.ucfgroup.client.beetle.bean.getnodeinforesponse.*;

/**
 * generated by hframework on 2016-04-22.
 */
public class GetNodeInfoResponse   {

	@JsonProperty("error_code")
	private String errorCode;
	@JsonProperty("error_msg")
	private String errorMsg;
	@JsonProperty("data")
	private Data data;
	@XStreamOmitField
	private boolean converted;

    public GetNodeInfoResponse() {
    }
 	
	public GetNodeInfoResponse convert()  throws Exception{
			if(!converted) {
			   String beforeInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(beforeInfo);
			   converted = true;
			   String afterInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(afterInfo);
			}
			return this;
	}

  
 	 	 
     public String getErrorCode(){
     	return errorCode;
     }

     public void setErrorCode(String errorCode){
     	this.errorCode = errorCode;
     }
	 	 	 
     public String getErrorMsg(){
     	return errorMsg;
     }

     public void setErrorMsg(String errorMsg){
     	this.errorMsg = errorMsg;
     }
	 	 	 
     public Data getData(){
     	return data;
     }

     public void setData(Data data){
     	this.data = data;
     }
	 	 
}