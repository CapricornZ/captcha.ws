package onstar.gvs;

public class Server {

	private String name;
	private String ip;
	private String userName;
	private String password;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Server(String name, String ip, String user, String pass){
		
		this.name = name;
		this.ip = ip;
		this.userName = user;
		this.password = pass;
	}
}
