package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;

public interface ILiveContentSelectMng {
	public List<ContentSelect> getListByType(Integer contentType);
	public Integer saveselect(ContentSelect content);
	public void saveselectfrompublish(ContentSelect content);
	public void removeselect(Integer id);
	public void saveindex(List<Map<String,String>> list);
	public List<ContentSelect> getList();
	public Pagination getPager(Integer contentType,Integer type,Integer pageNo, Integer pageSize);
	public List<ContentSelect> getListByShows(Integer shows);
	public List<ContentSelect> getListByShows(Integer shows,Integer pageNo,Integer enterpriseId);
	public List<ContentSelect> getListByShowsAndEnid(Integer shows,Integer enterpriseId);
	public List<ContentSelect> getNumByShows(Integer shows,Integer num,Integer enterpriseId);
	public void saveTitle2(String title2,Integer shows,Integer enterpriseId);
	public void updateTitle2(String title2,Integer shows,Integer enterpriseId);
	public void saveLink(String link,String linkName,Integer enterpriseId,Integer shows);
	/**
	 * 保存背景图片
	 * @param imgurl
	 * @param enterpriseId
	 * @param structureId
	 */
	public void saveImg(String imgurl,Integer enterpriseId,Integer structureId);
	public void updateLink(String link,String linkName,Integer enterpriseId,Integer shows);
	/**
	 * 更新图片
	 * @param imgurl
	 * @param enterpriseId
	 * @param structureId
	 */
	public void updateImg(String imgurl,Integer enterpriseId,Integer structureId);
	public void deletelink(Integer enterpriseId,Integer structureId);
	//根据企业id和结构id获取内容
	public List<ContentSelect> getContentByEnterpriseAndStructure(Integer enterpriseId,Integer structureId);
	
	public Pagination getPageContentByEnterpriseAndStructure(Integer pageSize,Integer pageNo,Integer enterpriseId,Integer structureId);
	
	
	public List<ContentSelect> getContentByEnterprise(Integer enterpriseId);
	//获取发布以后的信息
	public List<ContentSelectPublish> getPublishContentByEnterprise(Integer enterpriseId);
	//保存发布的信息
	public void savePublishContent(ContentSelect contentSelectPublish);
	//保存发布的结构
	public void savePublishStructure(ILiveHomepageStructure iLiveHomepageStructure);
	public void saveStructure(ILiveHomepageStructure iLiveHomepageStructure);
	public void saveHomepageStructure(ILiveHomepageStructure iLiveHomepageStructure);
	//根据企业id删除发布的信息
	public void deletePublishContent(Integer enterpriseId);
	public void deleteContentByEnterprise(Integer enterpriseId);
	//根据企业id删除发布的结构
	public void deletePublishStructure(Integer enterpriseId);
	public void deleteStructure(Integer enterpriseId);
	//
	public List<ILiveHomepageStructurePublish> getPublishStructureByEnterprise(Integer enterpriseId);
	
	//根据企业id获取结构
	public List<ILiveHomepageStructure> getStructureByEnterprise(Integer enterpriseId);
	
	//保存分割线行 标题
	public void savecontentName(String contentName,Integer enterpriseId,Integer structureId);
	public void updatecontentName(String contentName,Integer enterpriseId,Integer structureId);
	//保存分割线行的url和url名称
	public void saveContetnLink(String contentUrl, String urlName,Integer enterpriseId,Integer structureId);
	public void updateContetnLink(String contentUrl, String urlName,Integer enterpriseId,Integer structureId);
	//更新当前结构中显示的数量
	public void updateStructureNum(Integer showNum,Integer policy ,Integer structureId,Integer enterpriseId);
	//根据结构id获取结构信息
	public ILiveHomepageStructure getStructureById(Integer structureId,Integer enterpriseId);
	//根据结构id获取发布的结构信息
	public ILiveHomepageStructurePublish getPublishStructureById(Integer structureId,Integer enterpriseId);
	//根据id更新显示内容类型
	public void updaeStructureContentType(Integer showContentType,Integer structureId,Integer enterpriseId);

	/*
	 * 根据企业id和结构id删除编辑库内容
	 */
	public void deleteContentByEnterpriseIdAndStructure(Integer enterpriseId,Integer structureId);
	
	/*
	 * 视频删除时同时删掉主页要显示的信息
	 */
	public void deleteContentPublish(Long[] contentId);
	
	public ContentSelectPublish getContentPublishById(Integer id);
	public void updateContentPublish(ContentSelectPublish contentSelectPublish);
	
	public ContentSelect getContentById(Integer id);
	public void updateContent(ContentSelect contentSelect);
}
