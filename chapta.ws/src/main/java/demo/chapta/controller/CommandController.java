package demo.chapta.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chapta.model.Client;
import demo.chapta.service.IClient;

@RequestMapping(value = "/command")
@Controller
public class CommandController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

	private IClient clientService;
	public void setClientService(IClient clientServ){
		this.clientService = clientServ;
	}
	
	@RequestMapping(value = "/keepAlive.do",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Client accept(@RequestParam("ip")String ip) {
		
		logger.info(ip);
		Client client = new Client();
		client.setIp(ip);
		client.setUpdateTime(new Date());
		client = this.clientService.save(client);
		
		return client;
	}
	
	@RequestMapping(value = "/configuration.do",method=RequestMethod.GET)
	@ResponseBody
	public Config config(String ip) {
		
		return new Config(new Date());
	}
}
