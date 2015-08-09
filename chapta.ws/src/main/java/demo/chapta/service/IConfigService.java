package demo.chapta.service;

import java.util.List;

import demo.chapta.model.Client;
import demo.chapta.model.Config;

public interface IConfigService {
	
	Config saveOrUpdate(Config config);
	Config saveOrUpdate(Config config, Client client);
	List<Config> listAll();
	void delete(String config);
}
