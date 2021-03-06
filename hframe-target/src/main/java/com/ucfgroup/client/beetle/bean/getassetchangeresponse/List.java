package com.ucfgroup.client.beetle.bean.getassetchangeresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hframework.common.util.message.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


public class List   {

	@JsonProperty("id")
	private String id;
	@JsonProperty("log_time")
	private String logTime;
	@JsonProperty("log_info")
	private String logInfo;
	@JsonProperty("note")
	private String note;
	@JsonProperty("record_num")
	private String recordNum;
	@JsonProperty("lock_money")
	private String lockMoney;
	@JsonProperty("money")
	private String money;
	@JsonProperty("info")
	private String info;
	@JsonProperty("date")
	private String date;
	@JsonProperty("log_date")
	private String logDate;
	@XStreamOmitField
	private boolean converted;

    public List() {
    }
 
	public List convert()  throws Exception{
			if(!converted) {
			   String beforeInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(beforeInfo);
			   converted = true;
			   String afterInfo = XmlUtils.writeValueAsString(this);
			   System.out.println(afterInfo);
			}
			return this;
	}

  
 	 	 
     public String getId(){
     	return id;
     }

     public void setId(String id){
     	this.id = id;
     }
	 	 	 
     public String getLogTime(){
     	return logTime;
     }

     public void setLogTime(String logTime){
     	this.logTime = logTime;
     }
	 	 	 
     public String getLogInfo(){
     	return logInfo;
     }

     public void setLogInfo(String logInfo){
     	this.logInfo = logInfo;
     }
	 	 	 
     public String getNote(){
     	return note;
     }

     public void setNote(String note){
     	this.note = note;
     }
	 	 	 
     public String getRecordNum(){
     	return recordNum;
     }

     public void setRecordNum(String recordNum){
     	this.recordNum = recordNum;
     }
	 	 	 
     public String getLockMoney(){
     	return lockMoney;
     }

     public void setLockMoney(String lockMoney){
     	this.lockMoney = lockMoney;
     }
	 	 	 
     public String getMoney(){
     	return money;
     }

     public void setMoney(String money){
     	this.money = money;
     }
	 	 	 
     public String getInfo(){
     	return info;
     }

     public void setInfo(String info){
     	this.info = info;
     }
	 	 	 
     public String getDate(){
     	return date;
     }

     public void setDate(String date){
     	this.date = date;
     }
	 	 	 
     public String getLogDate(){
     	return logDate;
     }

     public void setLogDate(String logDate){
     	this.logDate = logDate;
     }
	 	 
}
