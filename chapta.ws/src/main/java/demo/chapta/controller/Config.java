package demo.chapta.controller;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Config{
	
	private Date startTime;
	public Config(Date start){
		this.startTime = start;
	}
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)  
	public Date getStartTime(){
		return this.startTime;
	}
}