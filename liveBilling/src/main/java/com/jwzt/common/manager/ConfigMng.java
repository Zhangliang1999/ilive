package com.jwzt.common.manager;

import java.util.Map;

import com.jwzt.common.entity.Config;

public interface ConfigMng {
	/**
	 * 获取所有配置
	 * 
	 * @return
	 */
	public Map<String, String> getMap();

	/**
	 * 按id获取配置值
	 * 
	 * @param id
	 * @return
	 */
	public String getValue(String id);

	/**
	 * 批量更新或新增配置
	 * 
	 * @param map
	 */
	public void updateOrSave(Map<String, String> map);

	/**
	 * 更新或新增配置
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Config updateOrSave(String key, String value);

	/**
	 * 删除配置
	 * 
	 * @param id
	 * @return
	 */
	public Config deleteById(String id);

	/**
	 * 批量删除配置
	 * 
	 * @param ids
	 * @return
	 */
	public Config[] deleteByIds(String[] ids);
}