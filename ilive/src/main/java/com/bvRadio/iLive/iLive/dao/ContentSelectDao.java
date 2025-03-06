package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ContentSelect;

public interface ContentSelectDao {
	public ContentSelect getById(Integer id);
	public List<ContentSelect> getListByType(Integer contentType);
	public void saveselect(ContentSelect content);
	public void removeselect(Integer id);
	public void saveindex(Integer id,Integer index);
	public List<ContentSelect> getList();
	public Pagination getPager(Integer contentType,Integer type,Integer pageNo, Integer pageSize);
	public List<ContentSelect> getListByShows(Integer shows);
	public List<ContentSelect> getListByShows(Integer shows,Integer pageNo,Integer enterpriseId);
	public List<ContentSelect> getNumByShows(Integer shows, Integer num,Integer enterpriseId);
	public List<ContentSelect> getListByShowsAndEnid(Integer shows, Integer enterpriseId);
	/**
	 * 根据企业id和结构id获取内容
	 * @param enterpriseId
	 * @param structureId
	 * @return
	 */
	public List<ContentSelect> getContentByEnterpriseAndStructure(Integer enterpriseId, Integer structureId);
	public void updateImg(String imgurl,Integer enterpriseId,Integer structureId);
	//根据企业id获取所有内容
	public List<ContentSelect> getContentByEnterprise(Integer enterpriseId);
	//根据企业id和结构id更新分割线标题
	public void updatecontentName(String contentName, Integer enterpriseId, Integer structureId);
	//根据企业id和结构id更新分割线行链接
	public void updateContetnLink(String contentUrl, String urlName, Integer enterpriseId, Integer structureId);
	//根据企业id和结构id删除链接
	public void deletelink(Integer enterpriseId,Integer structureId);
	public void deleteContentByEnterprise(Integer enterpriseId);
	
	/*
	 * 根据企业id和结构id删除内容
	 */
	public void deleteContentByEnterpriseIdAndStructure(Integer enterpriseId, Integer structureId);
	
	public void update(ContentSelect contentSelect);
	
	public Pagination getPageContentByEnterpriseAndStructure(Integer pageSize, Integer pageNo, Integer enterpriseId,
			Integer structureId) ;
	
}
