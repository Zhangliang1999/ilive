package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.BBSDiyformData;

public interface BBSDiyformDataMng {

	/**
	 * 新增一条数据
	 * 
	 * @param bbsDiyformData
	 */
	public void saveBBSDiyfromData(BBSDiyformData bbsDiyformData);

	public boolean checkUserSignUp(String userId, Integer formId);

	public void saveBBSDiyfromData(List<BBSDiyformData> diyformDatas);

}
