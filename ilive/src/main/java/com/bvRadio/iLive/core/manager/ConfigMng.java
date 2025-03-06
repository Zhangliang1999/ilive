package com.bvRadio.iLive.core.manager;

import java.util.Map;

import com.bvRadio.iLive.core.entity.Config;

public interface ConfigMng {
	public Map<String, String> getMap();

	public String getValue(String id);

	public void updateOrSave(Map<String, String> map);

	public Config updateOrSave(String key, String value);

	public Config deleteById(String id);

	public Config[] deleteByIds(String[] ids);
}