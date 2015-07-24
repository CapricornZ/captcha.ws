package demo.chapta.controller;

import onstar.gvs.Server;
import onstar.gvs.VehComm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.jsch.JschHandler;
import demo.jsch.ReturnCode;
import demo.jsch.SshUserInfo;

@RequestMapping(value = "/vehcomm")
@Controller
public class VcommController {
	
	@RequestMapping(method=RequestMethod.GET, value = "/index.do")
	public String index(){
		return "list";
	}

	@RequestMapping(method=RequestMethod.GET, value = "/{instance}/status.do")
	@ResponseBody
	public String getStatus(@PathVariable("instance")String instance){
		
		/*ReturnCode rtnCode = null;
		try{
			SshUserInfo userInfo = new SshUserInfo("Pass2012");
			JschHandler jhandler = new JschHandler("Bvcomm","111.235.97.12",22,userInfo);
			jhandler.init();
			rtnCode = jhandler.exec(String.format("admin/bin/solaris-AdminScript.sh 7 %s", instance));
			jhandler.destory();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return rtnCode != null ? rtnCode.getOutput() : "ERROR";*/
		Server server = new Server(instance, "111.235.97.12", "Bvcomm", "Pass2012");
		VehComm veh = new VehComm();
		veh.setName(instance);
		veh.setServer(server);

		try{
			return "" + veh.isRunning();
			
		} catch (Exception ex){
			return "false";
		}
	}

	@RequestMapping(method=RequestMethod.GET, value = "/{instance}/detail.do")
	@ResponseBody
	public String getDetail(@PathVariable("instance")String instance){
		
		ReturnCode rtnCode = null;
		try{
			SshUserInfo userInfo = new SshUserInfo("Pass2012");
			JschHandler jhandler = new JschHandler("Bvcomm","111.235.97.12",22,userInfo);
			jhandler.init();
			rtnCode = jhandler.exec(String.format("admin/bin/solaris-AdminScript.sh 14 %s", instance));
			jhandler.destory();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return rtnCode != null ? rtnCode.getOutput() : "ERROR";
	}
}
