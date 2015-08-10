package demo.chapta.model;

public class LoginOperation extends Operation{

	public LoginOperation(){
		this.setType("LOGIN");
	}
	
	@Override
	public String getTips() {
		
		return String.format("@%s", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getStartTime()));
	}
}
