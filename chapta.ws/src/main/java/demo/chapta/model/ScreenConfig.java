package demo.chapta.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.util.CustomDateDeserializer;
import demo.chapta.util.CustomDateSerializer;

public class ScreenConfig {

	private int id;
	private String category;
	private String fromHost;
	private String jsonContent;
	private Date createTime;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }
	
	public String getFromHost() { return fromHost; }
	public void setFromHost(String fromHost) { this.fromHost = fromHost; }
	
	public String getJsonContent() { return jsonContent; }
	public void setJsonContent(String jsonContent) { this.jsonContent = jsonContent; }
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCreateTime() { return createTime; }
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
