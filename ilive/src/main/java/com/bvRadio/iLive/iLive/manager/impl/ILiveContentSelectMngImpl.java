package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ContentPolicyDao;
import com.bvRadio.iLive.iLive.dao.ContentSelectDao;
import com.bvRadio.iLive.iLive.dao.ContentSelectPublishDao;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageStructureDao;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageStructurePublishDao;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class ILiveContentSelectMngImpl implements ILiveContentSelectMng {

	@Autowired
	private ContentSelectDao contentDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Autowired
	private ContentPolicyDao policyDao;
	
	@Autowired
	private ILiveHomepageStructureDao ilievStrictureDao;
	
	@Autowired
	private ContentSelectPublishDao contentSelectPublishDao;
	
	@Autowired
	private ILiveHomepageStructurePublishDao iLiveHomepageStructurePublishDao;
	
	@Override
	public List<ContentSelect> getListByType(Integer contentType) {
		return contentDao.getListByType(contentType);
	}

	@Override
	public Integer saveselect(ContentSelect content) {
		Integer nextId = filedIdMng.getNextId("content_select", "id", 1).intValue();
		content.setId(nextId);
		contentDao.saveselect(content);
		return nextId;
	}
	
	@Override
	public void saveselectfrompublish(ContentSelect content) {
		ContentSelect contentSelect = contentDao.getById(content.getId());
		if(contentSelect == null) {
			contentDao.saveselect(content);
		}
	}

	@Override
	public void removeselect(Integer id) {
		contentDao.removeselect(id);
	}

	@Override
	public void saveindex(List<Map<String, String>> list) {
		for(int i=0;i<list.size();i++) {
			Map<String,String> map =list.get(i);
			Integer id = Integer.parseInt((String) map.get("id"));
			Integer index = Integer.parseInt((String) map.get("index"));
			contentDao.saveindex(id,index);
		}
	}

	@Override
	public List<ContentSelect> getList() {
		return contentDao.getList();
	}

	@Override
	public Pagination getPager(Integer contentType,Integer type,Integer pageNo, Integer pageSize) {
		return contentDao.getPager(contentType,type,pageNo,pageSize);
	}

	@Override
	public List<ContentSelect> getListByShows(Integer shows) {
		return contentDao.getListByShows(shows);
	}
	
	@Override
	public List<ContentSelect> getListByShows(Integer shows,Integer pageNo,Integer enterpriseId) {
		return contentDao.getListByShows(shows,pageNo,enterpriseId);
	}

	@Override
	public List<ContentSelect> getNumByShows(Integer shows, Integer num,Integer enterpriseId) {
		return contentDao.getNumByShows(shows,num,enterpriseId);
	}

	@Override
	public void saveTitle2(String title2,Integer shows,Integer enterpriseId) {
		Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
		ContentPolicy policy = new ContentPolicy();
		policy.setId(nextId);
		policy.setShows(shows);
		policy.setTitle2(title2);
		policy.setEnterpriseId(enterpriseId);
		policyDao.saveTitle2(policy);
	}

	@Override
	public void updateTitle2(String title2,Integer shows,Integer enterpriseId) {
		policyDao.updateTitle2(title2,shows,enterpriseId);
	}

	@Override
	public void saveLink(String link, String linkName,Integer enterpriseId,Integer shows) {
		Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
		ContentPolicy policy = new ContentPolicy();
		policy.setId(nextId);
		policy.setShows(shows);
		policy.setLink(linkName);
		policy.setLinkName(linkName);
		policy.setEnterpriseId(enterpriseId);
		policyDao.saveLink(policy);
	}

	@Override
	public void updateLink(String link, String linkName,Integer enterpriseId,Integer shows) {
		policyDao.updateLink(link,linkName,enterpriseId,shows);
	}

	@Override
	public void deletelink(Integer enterpriseId,Integer structureId) {
		contentDao.deletelink(enterpriseId,structureId);
	}

	@Override
	public void saveImg(String imgurl,Integer enterpriseId,Integer structureId) {
		Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
		ContentSelect con = new ContentSelect();
		con.setId(nextId);
		con.setContentImg(imgurl);
		con.setEnterpriseId(enterpriseId);
		con.setStructureId(structureId);
		contentDao.saveselect(con);
	}

	@Override
	public void updateImg(String imgurl,Integer enterpriseId,Integer structureId) {
		contentDao.updateImg(imgurl,enterpriseId,structureId);
	}


	@Override
	public List<ContentSelect> getListByShowsAndEnid(Integer shows, Integer enterpriseId) {
		return contentDao.getListByShowsAndEnid(shows,enterpriseId);
	}

	@Override
	public List<ContentSelect> getContentByEnterpriseAndStructure(Integer enterpriseId, Integer structureId) {
		return contentDao.getContentByEnterpriseAndStructure(enterpriseId,structureId);
	}

	@Override
	public List<ILiveHomepageStructure> getStructureByEnterprise(Integer enterpriseId) {
		return ilievStrictureDao.getStructureByEnterprise(enterpriseId);
	}

	@Override
	public List<ContentSelect> getContentByEnterprise(Integer enterpriseId) {
		return contentDao.getContentByEnterprise(enterpriseId);
	}

	@Override
	public void savecontentName(String contentName, Integer enterpriseId, Integer structureId) {
		ContentSelect content = new ContentSelect();
		Integer nextId = filedIdMng.getNextId("content_select", "id", 1).intValue();
		content.setId(nextId);
		content.setContentName(contentName);
		content.setEnterpriseId(enterpriseId);
		content.setStructureId(structureId);
		contentDao.saveselect(content);
	}

	@Override
	public void updatecontentName(String contentName, Integer enterpriseId, Integer structureId) {
		contentDao.updatecontentName(contentName, enterpriseId, structureId);
	}

	@Override
	public void saveContetnLink(String contentUrl, String urlName, Integer enterpriseId, Integer structureId) {
		ContentSelect content = new ContentSelect();
		Integer nextId = filedIdMng.getNextId("content_select", "id", 1).intValue();
		content.setId(nextId);
		content.setContentUrl(contentUrl);
		content.setUrlName(urlName);
		content.setEnterpriseId(enterpriseId);
		content.setStructureId(structureId);
		contentDao.saveselect(content);
	}

	@Override
	public void updateContetnLink(String contentUrl, String urlName, Integer enterpriseId, Integer structureId) {
		contentDao.updateContetnLink(contentUrl,urlName,enterpriseId,structureId);
	}

	@Override
	public void updateStructureNum(Integer showNum,Integer policy, Integer structureId,Integer enterpriseId) {
		ilievStrictureDao.updateStructureNum(showNum,policy,structureId,enterpriseId);
	}

	@Override
	public ILiveHomepageStructure getStructureById(Integer structureId,Integer enterpriseId) {
		return ilievStrictureDao.getStructureById(structureId,enterpriseId);
	}

	@Override
	public void updaeStructureContentType(Integer showContentType, Integer structureId, Integer enterpriseId) {
		ilievStrictureDao.updaeStructureContentType(showContentType,structureId,enterpriseId);
	}

	@Override
	public List<ContentSelectPublish> getPublishContentByEnterprise(Integer enterpriseId) {
		return contentSelectPublishDao.getPublishContentByEnterprise(enterpriseId);
	}

	@Override
	public void savePublishContent(ContentSelect contentSelectPublish) {
		ContentSelectPublish contentSelectPublish2 = contentSelectPublishDao.getById(contentSelectPublish.getId());
		if(contentSelectPublish2 == null) {
			contentSelectPublishDao.savePublishContent(contentSelectPublish);
		}
		
	}

	@Override
	public void deletePublishContent(Integer enterpriseId) {
		contentSelectPublishDao.deletePublishContent(enterpriseId);
	}

	@Override
	public void deletePublishStructure(Integer enterpriseId) {
		iLiveHomepageStructurePublishDao.deletePublishStructure(enterpriseId);
	}

	@Override
	public List<ILiveHomepageStructurePublish> getPublishStructureByEnterprise(Integer enterpriseId) {
		return iLiveHomepageStructurePublishDao.getPublishStructureByEnterprise(enterpriseId);
	}

	@Override
	public void savePublishStructure(ILiveHomepageStructure iLiveHomepageStructure) {
		Integer nextId = filedIdMng.getNextId("ilive_homepage_structure_publish", "id", 1).intValue();
		ILiveHomepageStructurePublish structurePublish = new ILiveHomepageStructurePublish();
		iLiveHomepageStructure.setId(nextId);
		/*structurePublish.setStructureId(iLiveHomepageStructure.getStructureId());
		structurePublish.setOrders(iLiveHomepageStructure.getOrders());
		structurePublish.setType(iLiveHomepageStructure.getType());
		structurePublish.setEnterpriseId(iLiveHomepageStructure.getEnterpriseId());
		structurePublish.setPolicy(iLiveHomepageStructure.getPolicy());
		structurePublish.setShowNum(iLiveHomepageStructure.getShowNum());
		structurePublish.setShowContentType(iLiveHomepageStructure.getShowContentType());*/
		iLiveHomepageStructurePublishDao.savePublishStructure(iLiveHomepageStructure);
	}

	@Override
	public ILiveHomepageStructurePublish getPublishStructureById(Integer structureId,Integer enterpriseId) {
		return iLiveHomepageStructurePublishDao.getPublishStructureById(structureId,enterpriseId);
	}

	@Override
	public void deleteContentByEnterprise(Integer enterpriseId) {
		contentDao.deleteContentByEnterprise(enterpriseId);
	}

	@Override
	public void deleteStructure(Integer enterpriseId) {
		ilievStrictureDao.deleteStructure(enterpriseId);
		
	}

	@Override
	public void saveStructure(ILiveHomepageStructure iLiveHomepageStructure) {
		Integer nextId = filedIdMng.getNextId("ilive_homepage_structure", "id", 1).intValue();
		iLiveHomepageStructure.setId(nextId);
		ilievStrictureDao.saveStructure(iLiveHomepageStructure);
	}

	@Override
	public void deleteContentByEnterpriseIdAndStructure(Integer enterpriseId, Integer structureId) {
		contentDao.deleteContentByEnterpriseIdAndStructure(enterpriseId,structureId);
	}

	@Transactional
	@Override
	public void saveHomepageStructure(ILiveHomepageStructure iLiveHomepageStructure) {
		Integer nextId = filedIdMng.getNextId("ilive_homepage_structure", "id", 1).intValue();
		iLiveHomepageStructure.setId(nextId);
		ilievStrictureDao.saveStructure(iLiveHomepageStructure);
	}

	@Override
	public void deleteContentPublish(Long[] contentId) {
		for(long idLong:contentId) {
			int id = (int)idLong;
			ContentSelectPublish content = contentSelectPublishDao.getByContentId(id, 2);
			if(content!=null) {
				contentSelectPublishDao.deleteContentPublish(id);
			}
		}
	}

	@Override
	@Transactional
	public ContentSelectPublish getContentPublishById(Integer id) {
		return contentSelectPublishDao.getById(id);
	}

	@Override
	@Transactional
	public void updateContentPublish(ContentSelectPublish contentSelectPublish) {
		contentSelectPublishDao.update(contentSelectPublish);
	}

	@Override
	public ContentSelect getContentById(Integer id) {
		return contentDao.getById(id);
	}

	@Override
	public void updateContent(ContentSelect contentSelect) {
		contentDao.update(contentSelect);
	}

	@Override
	public Pagination getPageContentByEnterpriseAndStructure(Integer pageSize, Integer pageNo, Integer enterpriseId,
			Integer structureId) {
		return contentDao.getPageContentByEnterpriseAndStructure(pageSize,pageNo,enterpriseId,structureId);
	}


}
