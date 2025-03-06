package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;

public interface ILiveFCodeDao {
	//保存F码
	public boolean saveFCode(ILiveFCode liveCode);
	
	//查询所有F码
	public List<ILiveFCode> getAllFCode();
	
	//根据id删除F码
	public void deleteByCodeId(ILiveFCode liveFCode);
	
	//根据id查询一个F码
	public ILiveFCode getById(Long codeId);
	
	//根据codeId查询详细F码列表
	public List<ILiveFCodeDetail> getDetailByCodeId(Long codeId);

	public Pagination getMainPager(String userName, int cpn, int pageSize);
}
