package demo.chapta.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import demo.chapta.model.Client;

public class ClientService implements IClient {
	
	@Resource(name="sessionFactory")
    private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
    	
        this.sessionFactory = sessionFactory;
    }
    
    protected Session getSession() {
    	
        return sessionFactory.getCurrentSession();
    }
    
    public Client save(Client client){
    	
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

}
