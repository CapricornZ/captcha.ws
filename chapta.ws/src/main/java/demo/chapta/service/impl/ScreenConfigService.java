package demo.chapta.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import demo.chapta.model.ScreenConfig;
import demo.chapta.service.IScreenConfigService;
import demo.chapta.service.Service;

public class ScreenConfigService extends Service implements IScreenConfigService {

	@Override
	public ScreenConfig save(ScreenConfig screenConfig) {

		screenConfig.setCreateTime(new Date());
		this.getSession().saveOrUpdate(screenConfig);
		return screenConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScreenConfig> listAll() {

		String hql = "from ScreenConfig";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScreenConfig> listByCategory(String category) {
		
		String hql = "from ScreenConfig where category=:category";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("category", category);
		return query.list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ScreenConfig getByID(int id) {
		
		String hql = "from ScreenConfig where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);		
		List list = query.list();
		return (ScreenConfig)(list.size() > 0 ? list.get(0):null);
	}

}
