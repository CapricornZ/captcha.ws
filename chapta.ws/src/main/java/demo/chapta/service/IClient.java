package demo.chapta.service;

import demo.chapta.model.Client;

public interface IClient {
	
	Client save(Client client);
	void delete(Client client);
	void update(Client record);
	Client queryByIP(String ip);
}
