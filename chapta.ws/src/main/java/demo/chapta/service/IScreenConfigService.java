package demo.chapta.service;

import java.util.List;

import demo.chapta.model.ScreenConfig;

public interface IScreenConfigService {

	ScreenConfig save(ScreenConfig screenConfig);
	ScreenConfig getByID(int id);
	List<ScreenConfig> listByCategory(String category);
	List<ScreenConfig> listAll();
}
