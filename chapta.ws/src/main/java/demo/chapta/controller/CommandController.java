package demo.chapta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chapta.model.Client;
import demo.chapta.model.Operation;
import demo.chapta.service.IClientService;
import demo.chapta.controller.rest.*;

@RequestMapping(value = "/command")
@Controller
public class CommandController implements ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
	
	private IClientService clientService;
	public void setClientService(IClientService clientServ){ this.clientService = clientServ; }
	
	private GlobalConfig globalConfig;
	public void setGlobalConfig(GlobalConfig resource){ this.globalConfig = resource; }

	@RequestMapping(value = "/keepAlive.do")
	@ResponseBody
	public Client accept(@RequestParam("ip")String ip) {
		
		logger.info("accept keepAlive from 【{}】", ip);
		Client client = new Client(ip);
		client = this.clientService.saveOrUpdate(client);
		if(client.getOperation() != null){
			for(Operation op : client.getOperation())
				logger.debug("\tOPS : {}", op.getTips());
		}
		return client;
	}
	
	@RequestMapping(value = "/global.do", method={RequestMethod.GET})
	@ResponseBody
	public GlobalConfig getResource(@RequestParam("ip")String ip){
		
		logger.info("init Global Config from 【{}】", ip);
		return this.globalConfig;
	}
	
	@RequestMapping(value = "/resource/{ID}/modify", method={RequestMethod.PUT})
	@ResponseBody
	public void modifyResource(@RequestBody OrcConfig config, @PathVariable("ID")String id){
		
		OrcConfig obj = (OrcConfig)this.context.getBean(id);
		
		obj.setOffsetX(config.getOffsetX());
		obj.setOffsetY(config.getOffsetY());
		obj.setWidth(config.getWidth());
		obj.setHeight(config.getHeight());
		obj.setMinNearSpots(config.getMinNearSpots());
	}
	
	@RequestMapping(value = "/resource/preview", method={RequestMethod.GET})
	public String previewResource(Model model){

		model.addAttribute("PRICE", this.context.getBean("PRICE"));
		model.addAttribute("TIPS", this.context.getBean("TIPS"));
		model.addAttribute("TIPSNO", this.context.getBean("TIPS.NO"));
		model.addAttribute("LOADING", this.context.getBean("LOADING"));
		model.addAttribute("CAPTCHA", this.context.getBean("CAPTCHA"));
		model.addAttribute("LOGIN", this.context.getBean("LOGIN"));
		
		return "resource/preview";
	}

	private ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException { this.context = context; }
}
