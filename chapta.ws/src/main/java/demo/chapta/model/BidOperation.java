package demo.chapta.model;

public class BidOperation extends Operation {

	public BidOperation(){
		this.setType("BID");
	}
	
	private int price;
	public void setPrice(int price){ this.price = price; }
	public int getPrice(){ return this.price; }
	
	@Override
	public String getTips() {
		
		return String.format("+%d @%s", this.price, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getStartTime()));
	}
}
