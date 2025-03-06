package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.BBSDiyformOption;

public interface BBSDiyOptionDao {

	public BBSDiyformOption findById(Integer id);

	public BBSDiyformOption save(BBSDiyformOption bean);

}