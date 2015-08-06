package demo.chapta.controller.rest;

import demo.chapta.model.BidOperation;

public class CreateBidRequest {

	private int screenID;
	private String[] hosts;
	private BidOperation bid;
	
	public int getScreenID() { return screenID; }
	public void setScreenID(int screenID) { this.screenID = screenID; }
	
	public String[] getHosts() { return hosts; }
	public void setHosts(String[] hosts) { this.hosts = hosts; }
	
	public BidOperation getBid() { return bid; }
	public void setBid(BidOperation bid) { this.bid = bid; }
}
