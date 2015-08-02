package demo.chapta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chapta.model.Client;
import demo.chapta.service.IClientService;

@RequestMapping(value = "/command")
@Controller
public class CommandController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
	
	private IClientService clientService;
	public void setClientService(IClientService clientServ){
		this.clientService = clientServ;
	}

	@RequestMapping(value = "/keepAlive.do",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Client accept(@RequestParam("ip")String ip) {
		
		logger.info("accept keepAlive from 【{}】", ip);
		Client client = new Client();
		client.setIp(ip);
		client = this.clientService.saveOrUpdate(client);
		return client;
	}
}
