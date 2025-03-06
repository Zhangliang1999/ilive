package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface BBSDiyformDataMng {

	/**
	 * 新增一条数据
	 * 
	 * @param bbsDiyformData
	 */
	public void saveBBSDiyfromData(BBSDiyformData bbsDiyformData);

	/**
	 * 
	 * @param userId
	 * @param formId
	 * @return
	 */
	public boolean checkUserSignUp(String userId, Integer formId);

	/**
	 * 
	 * @param diyformDatas
	 */
	public void saveBBSDiyfromData(List<BBSDiyformData> diyformDatas);

	
	/**
	 * 获取前20条用户
	 * 
	 * @return
	 */
	public List<ILiveManager> distinctDiyformUser(Integer formId, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 获取前20条用户 条件加上用户名
	 * 
	 * @return
	 */
	public List<ILiveManager> distinctDiyformUserAndUsername(String username,Integer formId, Integer pageNo, Integer pageSize);

	
	/**
	 * 获取一条用户对应的一条数据
	 */
	public String[] getFormStatic(ILiveManager manager, Integer[] questionIds);
	
	/**
	 * 获得所有表单的提交ID
	 * @param formId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination distinctDiyformSubmitId(Integer formId, Date startTime, Date endTime, Integer pageNo, Integer pageSize);
	
	/**
	 * 获得所有表单的提交ID
	 * @param formId
	 * @return
	 */
	public List<Long> distinctDiyformSubmitId(Integer formId, Date startTime, Date endTime);
	
	/**
	 * 根据提交ID查问题
	 * @param submitId
	 * @param questionIds
	 * @return
	 */
	public String[] getFormStatic(Long submitId, Integer[] questionIds);
	
}
