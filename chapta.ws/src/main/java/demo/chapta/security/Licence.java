package demo.chapta.security;

public class Licence {
	
	private Valid period;
	private String signature;

	public String getSignature() { return signature; }
	public void setSignature(String signature) { this.signature = signature; }

	public Valid getPeriod() { return period; }
	public void setPeriod(Valid period) { this.period = period; }
}
