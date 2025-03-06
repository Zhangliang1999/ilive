package com.jwzt.common.dao;

import java.util.List;

import com.jwzt.common.entity.Config;

public interface ConfigDao {
	/**
	 * 获取所有配置项
	 * 
	 * @return
	 */
	public List<Config> getList();

	/**
	 * 按id获取配置
	 * 
	 * @param id
	 * @return
	 */
	public Config findById(String id);

	/**
	 * 保存配置
	 * 
	 * @param bean
	 * @return
	 */
	public Config save(Config bean);

	/**
	 * 删除配置
	 * 
	 * @param id
	 * @return
	 */
	public Config deleteById(String id);
}