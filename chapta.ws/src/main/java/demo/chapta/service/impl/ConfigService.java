package demo.chapta.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import demo.chapta.model.Client;
import demo.chapta.model.Config;
import demo.chapta.service.IConfigService;
import demo.chapta.service.Service;

public class ConfigService extends Service implements IConfigService {
    
	@Override
	public Config saveOrUpdate(Config config) {
		
		String hql = "from Config where no=:no";
    	Query query = this.getSession().createQuery(hql);
    	query.setParameter("no", config.getNo());
    	
    	@SuppressWarnings("unchecked")
		List<Config> list = query.list();
    	if(list.size()>0){
    		
    		Config tmpConfig = list.get(0);
    		tmpConfig.setUpdateTime(new Date());
    		this.getSession().update(tmpConfig);
    		return tmpConfig;
    	} else {
    		config.setUpdateTime(new Date());
    		this.getSession().saveOrUpdate(config);
    		return config;
    	}
	}

	@Override
	public Config saveOrUpdate(Config config, Client client) {

		config = this.saveOrUpdate(config);
		client.setConfig(config);
		client.setUpdateTime(new Date());
		this.getSession().update(client);
		return config;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Config> listAll() {
		
		return (List<Config>)this.getSession().createQuery("from Config").list();
	}

	@Override
	public void delete(String config) {
		
		Config cfg = new Config();
		cfg.setNo(config);
		this.getSession().delete(cfg);
	}
}
