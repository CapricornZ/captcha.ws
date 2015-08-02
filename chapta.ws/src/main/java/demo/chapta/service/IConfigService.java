package demo.chapta.service;

import demo.chapta.model.Client;
import demo.chapta.model.Config;

public interface IConfigService {
	
	Config saveOrUpdate(Config config);
	Config saveOrUpdate(Config config, Client client);
}
