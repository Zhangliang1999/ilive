package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;

public interface ILiveContentSelectMng {
	public List<ContentSelect> getListByType(Integer contentType);
	public Integer saveselect(ContentSelect content);
	public void removeselect(Integer id);
	public void saveindex(List<Map<String,String>> list);
	public ContentPolicy getPolicy();
	public void updatePolicy(ContentPolicy policy);
	public List<ContentSelect> getList();
	public Pagination getPager(Integer contentType,Integer type,Integer pageNo, Integer pageSize);
	public List<ContentSelect> getListByShows(Integer shows);
	public List<ContentSelect> getListByShows(Integer shows,Integer pageNo,Integer enterpriseId);
	public List<ContentSelect> getListByShowsAndEnid(Integer shows,Integer enterpriseId);
	public List<ContentSelect> getNumByShows(Integer shows,Integer num,Integer enterpriseId);
	public ContentPolicy getPolicyByShows(Integer shows,Integer enterpriseId);
	public ContentPolicy gettitle2(Integer shows);
	public ContentPolicy gettitle4(Integer shows);
	public void savepolicy(ContentPolicy policy);
	public void saveTitle2(String title2,Integer shows,Integer enterpriseId);
	public void updateTitle2(String title2,Integer shows,Integer enterpriseId);
	public void saveLink(String link,String linkName,Integer enterpriseId,Integer shows);
	public void saveImg(String imgurl,Integer enterpriseId);
	public void updateLink(String link,String linkName,Integer enterpriseId,Integer shows);
	public void updateImg(String imgurl,Integer enterpriseId);
	public void deletelink(Integer enterpriseId,Integer shows);
	public void deleteimg(Integer enterpriseId);
}
