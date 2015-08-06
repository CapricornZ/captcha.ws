package demo.chapta.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chapta.model.Client;
import demo.chapta.service.IClientService;

@RequestMapping(value = "/command")
@Controller
public class ClientController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	private IClientService clientService;
	public void setClientService(IClientService clientServ){
		this.clientService = clientServ;
	}
	
	@RequestMapping(value = "/client/stastic.do",method=RequestMethod.GET)
	public String stastic(Model model){
		 model.addAttribute("CLIENTS", this.clientService.list());
		 return "client/stastic";
	}
	
	@RequestMapping(value = "/client/list.do",method=RequestMethod.GET)
	@ResponseBody
	public List<Client> listClient(){
		
		List<Client> rtn = this.clientService.list();
		return rtn;
	}
	
	@RequestMapping(value = "/client/{HOST}/detail.do",method=RequestMethod.GET)
	public String detailClient(@PathVariable("HOST")String host, Model model){
		
		model.addAttribute("client", this.clientService.queryByIP(host));
		return "client/detail";
	}
	
	@RequestMapping(value = "/client/{host}/operation.do",method=RequestMethod.GET)
	public String clientOperation(@PathVariable("host")String host, Model model){
		
		Client client = this.clientService.queryByIP(host);
		model.addAttribute("client", client);
		return "operationOfClient";
	}
}