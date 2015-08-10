package demo.chapta.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import demo.chapta.model.Client;
import demo.chapta.model.Operation;
import demo.chapta.service.IClientService;
import demo.chapta.service.Service;

public class ClientService extends Service implements IClientService {
    
    public Client saveOrUpdate(Client client){
    	
    	String hql = "from Client where ip=:ip";
    	Query query = this.getSession().createQuery(hql);
    	query.setParameter("ip", client.getIp());
    	
    	@SuppressWarnings("unchecked")
		List<Client> list = query.list();
    	if(list.size()>0){
    		
    		Client tmpClient = list.get(0);
    		tmpClient.setUpdateTime(new Date());
    		this.getSession().update(tmpClient);
    		return tmpClient;
    	} else {
    		client.setUpdateTime(new Date());
    		this.getSession().saveOrUpdate(client);
    		return client;
    	}
    }
    
    public void delete(Client client){
    	
    	this.getSession().delete(client);
    }
    
    public void update(Client record){
    	
    	Client oldClient = (Client) this.getSession().get(Client.class, record.getIp());
    	if(null != oldClient){
    		oldClient.setUpdateTime(new Date());
    		this.getSession().update(oldClient);
    	}
    }
    
    public Client queryByIP(String ip){
    	
    	String hql = "from Client where ip=:ip";
    	Query query = this.getSession().createQuery(hql);
    	query.setParameter("ip", ip);
    	
    	@SuppressWarnings("unchecked")
    	List<Client> list = query.list();
    	return list.size()>0 ? list.get(0) : null;
    }

	@Override
	public List<Client> list() {
		
		String hql = "from Client";
    	Query query = this.getSession().createQuery(hql);
    	
    	@SuppressWarnings("unchecked")
    	List<Client> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> listByIPs(String[] hosts) {

		String hql = "from Client where ip in (:ips)";
    	Query query = this.getSession().createQuery(hql);
    	query.setParameterList("ips", hosts);

    	return query.list();
	}

	@Override
	public void removeOperation(String host, int operationID) {
		
		Query query = this.getSession().createQuery("from Operation where id=:id");
		@SuppressWarnings("unchecked")
		List<Operation> ops = query.setParameter("id", operationID).list();
		if(ops.size() > 0){
			
			Operation operation = ops.get(0);
			boolean bFound = false;
			for (Iterator iter = operation.getClients().iterator(); !bFound && iter.hasNext();) {
				
				Client client = (Client)iter.next();
				if(client.getIp().equals(host)){
					operation.getClients().remove(client);
					bFound = true;
				}
			}
			this.getSession().update(operation);
		}
	}

}
