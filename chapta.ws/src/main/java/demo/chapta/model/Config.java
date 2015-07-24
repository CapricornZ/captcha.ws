package demo.chapta.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.controller.CustomDateDeserializer;
import demo.chapta.controller.CustomDateSerializer;

public class Config {

	private String no;
	private String passwd;
	private String pid;
	private String pname;
	private Date startTime;
	private Date expireTime;
	private Date updateTime;
	
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getExpireTime() {
		return expireTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	private int price;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
