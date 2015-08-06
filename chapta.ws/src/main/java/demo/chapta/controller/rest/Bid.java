package demo.chapta.controller.rest;


/***
 * BID SCREEN CONFIG
 * @author martin
 *
 */
public class Bid {

	private GivePrice give;
	private SubmitPrice submit;
	
	public GivePrice getGive() {
		return give;
	}
	public void setGive(GivePrice give) {
		this.give = give;
	}
	public SubmitPrice getSubmit() {
		return submit;
	}
	public void setSubmit(SubmitPrice submit) {
		this.submit = submit;
	}
}
