package demo.chapta.service;

import java.util.List;

import demo.chapta.model.Client;

public interface IClientService {
	
	Client saveOrUpdate(Client client);
	void delete(Client client);
	void update(Client record);
	Client queryByIP(String ip);
	List<Client> list();
	List<Client> listByIPs(String[] hosts);
	
	void removeOperation(String host, int OperationID);
}
