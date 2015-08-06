package demo.chapta.controller.rest;

public class AssignOperation {
	
	private String[] hosts;
	int operationID;
	
	public void setHosts(String[] hosts){ this.hosts = hosts; }
	public String[] getHosts(){ return this.hosts; }
	
	public void setOperationID(int value){ this.operationID = value; }
	public int getOperationID(){ return this.operationID; }

}
