package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ContentSelect;

public interface ContentSelectDao {
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
}
