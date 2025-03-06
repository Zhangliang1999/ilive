package com.bvRadio.iLive.core.dao;

import java.util.List;

import com.bvRadio.iLive.core.entity.Config;

public interface ConfigDao {
	public List<Config> getList();

	public Config findById(String id);

	public Config save(Config bean);

	public Config deleteById(String id);
}