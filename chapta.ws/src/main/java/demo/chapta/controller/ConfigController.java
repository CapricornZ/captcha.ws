package demo.chapta.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chapta.model.Client;
import demo.chapta.service.IClientService;
import demo.chapta.service.IConfigService;

@RequestMapping(value = "/command/config")
@Controller
public class ConfigController {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	private IConfigService configService;
	public void setConfigService(IConfigService configService) {
		this.configService = configService;
	}
	
	private IClientService clientService;
	public void setClientService(IClientService clientServ){
		this.clientService = clientServ;
	}
	
	@RequestMapping(value = "/init.do", method=RequestMethod.GET)
	public String initConfig(Model model){
		
		//SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd");
		//String startTime = formatter.format(new Date()) + " 11:29:52";
		//String exipreTime = formatter.format(new Date()) + " 11:29:53";
		//model.addAttribute("startTime", startTime);
		//model.addAttribute("expireTime", exipreTime);
		
		model.addAttribute("configs", this.configService.listAll());
		return "preConfig";
	}
	
	@RequestMapping(value = "/delete.do",method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteConfig(@RequestParam("config")String configID){
		this.configService.delete(configID);
	}
	
	/***
	 * 创建标书、用户
	 * @param config
	 * @param clientHost
	 */
	@RequestMapping(value = "/new.do",method=RequestMethod.POST)
	@ResponseBody
	public void createConfig(@RequestBody demo.chapta.model.Config config, 
			@RequestParam("client")String clientHost){
		
		logger.info("Create new Config {user:{}}. Assign to HOST:【{}】", config.getPname(), clientHost);
		if("".equals(clientHost)){			
			this.configService.saveOrUpdate(config);
		} else {
			Client client = this.clientService.queryByIP(clientHost);
			this.configService.saveOrUpdate(config, client);
		}
	}	
}
