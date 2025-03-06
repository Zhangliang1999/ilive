package com.bvRadio.iLive.iLive.dao;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface BBSDiyformDataDao {

	/**
	 * 保存提交表单数据
	 * 
	 * @param formData
	 */
	public void saveDiyformData(BBSDiyformData formData);

	public boolean checkUserSignUp(String userId, Integer formId);

	public void saveDiyFormList(List<BBSDiyformData> diyformDatas);

	public Pagination distinctDiyformUser(Integer formId, Integer pageNo, Integer pageSize);

	public String[] getFormStatic(ILiveManager manager, Integer[] questionIds);
	
	public Pagination distinctDiyformSunmitId(Integer formId, Date startTime, Date endTime, Integer pageNo, Integer pageSize);
	
	public List<Long> distinctDiyformSunmitId(Integer formId, Date startTime, Date endTime);
	
	public String[] getFormStaticBySubmitId(Long submitId, Integer[] questionIds);

}
