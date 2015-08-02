package demo.chapta.model;

import java.util.Date;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.util.CustomDateDeserializer;
import demo.chapta.util.CustomDateSerializer;

public abstract class Operation {
	
	private int id;
	private String type;
	private Date startTime;
	private Date expireTime;
	private String content;
	private Date updateTime;
	
	@JsonIgnore
	private Set<Client> clients;
	public void setClients(Set<Client> clients){ this.clients = clients; }
	public Set<Client> getClients(){ return this.clients; }
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() { return updateTime; }
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getStartTime() { return startTime; }
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setStartTime(Date startTime) { this.startTime = startTime; }
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getExpireTime() { return expireTime; }
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
	
	@JsonIgnore
	public String getTips(){
		return "这个方法还没有实现";
	}
}
