package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ContentPolicyDao;
import com.bvRadio.iLive.iLive.dao.ContentSelectDao;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
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
	
	@Override
	public List<ContentSelect> getListByType(Integer contentType) {
		return contentDao.getListByType(contentType);
	}

	@Override
	public Integer saveselect(ContentSelect content) {
		Integer nextId = filedIdMng.getNextId("content_select", "id", 1).intValue();
		content.setId(nextId);
		content.setType(1);
		contentDao.saveselect(content);
		return nextId;
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
	public ContentPolicy getPolicy() {
		return policyDao.getPolicy();
	}

	@Override
	public void updatePolicy(ContentPolicy policy) {
		ContentPolicy p = policyDao.getPolicy();
		if(p==null) {
			Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
			policy.setId(nextId);
			policyDao.save(policy);
		}else {
			policyDao.updatePolicy(policy);
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
	public ContentPolicy getPolicyByShows(Integer shows,Integer enterpriseId) {
		return policyDao.getPolicyByShows(shows,enterpriseId);
	}

	@Override
	public void savepolicy(ContentPolicy policy) {
		ContentPolicy p = policyDao.getPolicyByShows(policy.getShows(),policy.getEnterpriseId());
		if(p==null) {
			Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
			policy.setId(nextId);
			policyDao.save(policy);
		}
		policyDao.updatePolicy(policy);
	}

	@Override
	public List<ContentSelect> getNumByShows(Integer shows, Integer num,Integer enterpriseId) {
		return contentDao.getNumByShows(shows,num,enterpriseId);
	}

	@Override
	public ContentPolicy gettitle2(Integer shows) {
		return policyDao.gettitle2(shows);
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
	public ContentPolicy gettitle4(Integer shows) {
		return policyDao.gettitle4(shows);
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
	public void deletelink(Integer enterpriseId,Integer shows) {
		policyDao.deletelink(enterpriseId,shows);
	}

	@Override
	public void saveImg(String imgurl,Integer enterpriseId) {
		Integer nextId = filedIdMng.getNextId("content_policy", "id", 1).intValue();
		ContentPolicy policy = new ContentPolicy();
		policy.setId(nextId);
		policy.setShows(1);
		policy.setImgurl(imgurl);
		policy.setEnterpriseId(enterpriseId);
		policyDao.save(policy);
	}

	@Override
	public void updateImg(String imgurl,Integer enterpriseId) {
		policyDao.updateImg(imgurl,enterpriseId);
	}

	@Override
	public void deleteimg(Integer enterpriseId) {
		policyDao.deleteimg(enterpriseId);
	}

	@Override
	public List<ContentSelect> getListByShowsAndEnid(Integer shows, Integer enterpriseId) {
		return contentDao.getListByShowsAndEnid(shows,enterpriseId);
	}

}
