package demo.jsch;

import com.jcraft.jsch.UserInfo;

public class SshUserInfo implements UserInfo {

	private String passphrase = null;

	public SshUserInfo(String passphrase) {
		super();
		this.passphrase = passphrase;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return passphrase;
	}

	public boolean promptPassphrase(String pass) {
		return true;
	}

	public boolean promptPassword(String pass) {
		return true;
	}

	public boolean promptYesNo(String arg0) {
		return true;
	}

	public void showMessage(String m) {
		System.out.println(m);
	}
}