package onstar.gvs;

import demo.jsch.JschHandler;
import demo.jsch.ReturnCode;
import demo.jsch.SshUserInfo;

public class VehComm implements IVehComm{
	
	private String name;
	private String dmb;
	private Server server;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDmb() {
		return dmb;
	}
	public void setDmb(String dmb) {
		this.dmb = dmb;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	
	@Override
	public boolean isRunning() throws Exception {
		
		SshUserInfo userInfo = new SshUserInfo("Pass2012");
		JschHandler jhandler = new JschHandler("Bvcomm","111.235.97.12",22,userInfo);
		jhandler.init();
		ReturnCode rtnCode = jhandler.exec(String.format("admin/bin/solaris-AdminScript.sh 7 %s", this.name));
		jhandler.destory();
		
		if(rtnCode.getCode() == 0){

			int index = rtnCode.getOutput().indexOf("is running");
			if(index > 0)
				return true;
		}
		return false;
	}
}
