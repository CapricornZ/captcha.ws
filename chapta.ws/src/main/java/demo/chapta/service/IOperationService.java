package demo.chapta.service;

import java.util.List;

import demo.chapta.model.Client;
import demo.chapta.model.Operation;

public interface IOperationService {

	Operation saveOrUpdate(Operation operation);
	List<Operation> listAll();
	Operation queryByID(int opsID);
	void deleteByID(int opsID);
	
	Operation saveOrUpdate(Operation operation, Client client);
	void updateClientOperation(Operation operation, List<Client> clients);
	
	void assign2Clients(int operationID, List<Client> hosts);
}
