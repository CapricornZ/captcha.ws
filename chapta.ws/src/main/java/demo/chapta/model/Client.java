package demo.chapta.model;

import java.util.Date;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.util.CustomDateDeserializer;
import demo.chapta.util.CustomDateSerializer;

public class Client {

	private String ip;
	private Date updateTime;
	private Config config;
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Client){
			Client other = (Client)obj;
			return other.ip.equals(this.ip);
		}
		return false;
		
	}
	
	public Client(){
	}
	
	public Client(String ip){
		this.ip = ip;
	}
	
	public Config getConfig() { return config; }
	public void setConfig(Config config) { this.config = config; }
	
	public String getIp() { return ip; }
	public void setIp(String ip) { this.ip = ip; }

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() { return updateTime; }
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
	
	private Set<Operation> operation;
	public void setOperation(Set<Operation> ops){
		this.operation = ops;
	}
	public Set<Operation> getOperation(){
		return this.operation;
	}
}
