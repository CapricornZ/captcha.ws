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
	
	@RequestMapping(value = "/resource/orc/tip/{ID}/modify", method={RequestMethod.PUT})
	@ResponseBody
	public void modifyTipConfig(@RequestBody OrcTipConfig config, @PathVariable("ID")String id){
		
		OrcTipConfig obj = (OrcTipConfig)this.context.getBean(id);
		
		obj.getConfigTip().setOffsetX(config.getConfigTip().getOffsetX());
		obj.getConfigTip().setOffsetY(config.getConfigTip().getOffsetY());
		obj.getConfigTip().setWidth(config.getConfigTip().getWidth());
		obj.getConfigTip().setHeight(config.getConfigTip().getHeight());
		obj.getConfigTip().setMinNearSpots(config.getConfigTip().getMinNearSpots());
		
		obj.getConfigNo().setOffsetX(config.getConfigNo().getOffsetX());
		obj.getConfigNo().setOffsetY(config.getConfigNo().getOffsetY());
		obj.getConfigNo().setWidth(config.getConfigNo().getWidth());
		obj.getConfigNo().setHeight(config.getConfigNo().getHeight());
		obj.getConfigNo().setMinNearSpots(config.getConfigNo().getMinNearSpots());
	}
	
	@RequestMapping(value = "/resource/orc/{ID}/modify", method={RequestMethod.PUT})
	@ResponseBody
	public void modifyConfig(@RequestBody OrcConfig config, @PathVariable("ID")String id){
		
		OrcConfig obj = (OrcConfig)this.context.getBean(id);
		
		obj.setOffsetX(config.getOffsetX());
		obj.setOffsetY(config.getOffsetY());
		obj.setWidth(config.getWidth());
		obj.setHeight(config.getHeight());
		obj.setMinNearSpots(config.getMinNearSpots());
	}
	
	@RequestMapping(value = "/resource/preview", method={RequestMethod.GET})
	public String previewResource(Model model){

		model.addAttribute("TAG", this.globalConfig.getTag());
		model.addAttribute("PRICE", this.context.getBean("PRICE"));
		model.addAttribute("TIPS0", this.context.getBean("TIPS0"));
		model.addAttribute("TIPS1", this.context.getBean("TIPS1"));
		model.addAttribute("LOADING", this.context.getBean("LOADING"));
		model.addAttribute("CAPTCHA", this.context.getBean("CAPTCHA"));
		model.addAttribute("LOGIN", this.context.getBean("LOGIN"));
		
		return "resource/preview";
	}

	private ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException { this.context = context; }
}
