package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;

public interface ILiveHomepageStructureDao {
	//根据企业id获取结构信息
	public List<ILiveHomepageStructure> getStructureByEnterprise(Integer enterpriseId);
	//根据结构id更新显示数量
	public void updateStructureNum(Integer showNum,Integer policy, Integer structureId,Integer enterpriseId);
	//根据结构id获取结构信息
	public ILiveHomepageStructure getStructureById(Integer structureId,Integer enterpriseId);
	//根据结构id和企业id更新显示内容类型
	public void updaeStructureContentType(Integer showContentType, Integer structureId, Integer enterpriseId);
	
	public void deleteStructure(Integer enterpriseId);
	public void saveStructure(ILiveHomepageStructure iLiveHomepageStructure);
	
	public ILiveHomepageStructure getById(Integer id);
}

