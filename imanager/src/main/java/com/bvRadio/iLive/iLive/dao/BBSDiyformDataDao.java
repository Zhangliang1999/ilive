package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.BBSDiyformData;

public interface BBSDiyformDataDao {
	
	/**
	 * 保存提交表单数据
	 * @param formData
	 */
	public void saveDiyformData(BBSDiyformData formData);

	public boolean checkUserSignUp(String userId, Integer formId);

	public void saveDiyFormList(List<BBSDiyformData> diyformDatas);

}
