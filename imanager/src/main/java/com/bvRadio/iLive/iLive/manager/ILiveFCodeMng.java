package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;

public interface ILiveFCodeMng {
	//添加F码
	public Long saveFCode(ILiveFCode liveFCode);
	
	//查找所有F码
	public List<ILiveFCode> getAllFCode();
	
	//根据id删除F码
	public void deleteByCodeId(Long codeId);
	
	//根据id查找F码
	public ILiveFCode getFCodeById(Long codeId);
	
	//获取F码列表
	public List<ILiveFCodeDetail> getDetailByCodeId(Long codeId);

	public Pagination getMainPager(String userName, int cpn, int pageSize);
}
