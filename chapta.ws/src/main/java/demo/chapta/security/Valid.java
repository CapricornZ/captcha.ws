package demo.chapta.security;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import demo.chapta.util.CustomDateDeserializer;
import demo.chapta.util.CustomDateSerializer;

public class Valid {
	
	private Date from;
	private Date to;

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getFrom() {
		return from;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setFrom(Date from) {
		this.from = from;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getTo() {
		return to;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setTo(Date to) {
		this.to = to;
	}
}
