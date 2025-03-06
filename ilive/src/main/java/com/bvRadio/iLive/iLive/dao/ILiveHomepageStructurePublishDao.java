package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;

public interface ILiveHomepageStructurePublishDao {
	public void deletePublishStructure(Integer enterpriseId);
	public List<ILiveHomepageStructurePublish> getPublishStructureByEnterprise(Integer enterpriseId);
	public void savePublishStructure(ILiveHomepageStructure iLiveHomepageStructure);
	public ILiveHomepageStructurePublish getPublishStructureById(Integer structureId,Integer enterpriseId);
	public ILiveHomepageStructurePublish getById(Integer id);
}
