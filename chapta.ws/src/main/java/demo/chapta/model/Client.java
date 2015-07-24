package demo.chapta.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.controller.CustomDateDeserializer;
import demo.chapta.controller.CustomDateSerializer;

public class Client {

	private String ip;
	private Date updateTime;
	private Config config;
	
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
