package demo.jsch;

public class ReturnCode {
	
	private int code;
	private String output;
	public ReturnCode(int code, String output){
		this.code = code;
		this.output = output;
	}
	
	public int getCode(){ return this.code; }
	public String getOutput() { return this.output; }

}
