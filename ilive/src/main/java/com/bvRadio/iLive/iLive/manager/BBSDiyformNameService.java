package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.BBSDiyformName;

public interface BBSDiyformNameService {

	void save(BBSDiyformName bbsDiyformName);
	
	BBSDiyformName get(Integer bbsDiyformId,Integer updateMark);
	
	BBSDiyformName getLast(Integer bbsDiyformId);
	
	void update(BBSDiyformName bbsDiyformName);
	
	List<BBSDiyformName> getListByFormId(Integer formId);
}
