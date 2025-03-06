package com.jwzt.common.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.dao.ConfigDao;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;



@Service
@Transactional
public class ConfigMngImpl implements ConfigMng {
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getMap() {
		List<Config> list = dao.getList();
		Map<String, String> map = new HashMap<String, String>(list.size());
		for (Config config : list) {
			map.put(config.getId(), config.getValue());
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public String getValue(String id) {
		Config entity = dao.findById(id);
		if (entity != null) {
			return entity.getValue();
		} else {
			return null;
		}
	}

	@Override
	public void updateOrSave(Map<String, String> map) {
		if (map != null && map.size() > 0) {
			for (Entry<String, String> entry : map.entrySet()) {
				updateOrSave(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public Config updateOrSave(String key, String value) {
		Config config = dao.findById(key);
		if (config != null) {
			config.setValue(value);
		} else {
			config = new Config(key, value);
			config.setValue(value);
			dao.save(config);
		}
		return config;
	}

	@Override
	public Config deleteById(String id) {
		Config bean = dao.deleteById(id);
		return bean;
	}

	@Override
	public Config[] deleteByIds(String[] ids) {
		Config[] beans = new Config[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	@Autowired
	private ConfigDao dao;

	@Autowired
	public void setDao(ConfigDao dao) {
		this.dao = dao;
	}
}