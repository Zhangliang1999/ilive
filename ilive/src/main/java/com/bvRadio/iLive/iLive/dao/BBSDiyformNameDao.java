package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.BBSDiyformName;

public interface BBSDiyformNameDao {

	void save(BBSDiyformName bbsDiyformName);
	
	BBSDiyformName get(Integer bbsDiyformId,Integer updateMark);
	
	BBSDiyformName getLast(Integer bbsDiyformId);
	
	void update(BBSDiyformName bbsDiyformName);
	
	List<BBSDiyformName> getListByFormId(Integer formId);
}
