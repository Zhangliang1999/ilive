package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;

public interface ContentSelectPublishDao {
	public List<ContentSelectPublish> getPublishContentByEnterprise(Integer enterpriseId);
	public void savePublishContent(ContentSelect contentSelectPublish);
	public void deletePublishContent(Integer enterpriseId);
	public ContentSelectPublish getById(Integer id);
	public ContentSelectPublish getByContentId(Integer contentId,Integer contentType);
	public void deleteContentPublish(Integer contentId);
	public void update(ContentSelectPublish contentSelectPublish);
}
