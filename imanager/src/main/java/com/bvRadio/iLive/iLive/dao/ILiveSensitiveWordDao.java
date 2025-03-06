package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;
import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveSensitiveWordDao {
	public Pagination getPage(String sensitiveName, int pageNo, int pageSize);

	public Pagination getPageByType(Integer type, int pageNo, int pageSize);
	public List<ILiveSensitiveWord> getList(Boolean disabled);
	
	public Pagination getPageByTypeAndName(Integer type, String sensitiveName, int pageNo,int pageSize);

	public ILiveSensitiveWord findById(Integer id);

	public ILiveSensitiveWord save(ILiveSensitiveWord bean);

	public ILiveSensitiveWord updateByUpdater(Updater<ILiveSensitiveWord> updater);

	public ILiveSensitiveWord deleteById(Integer id);

	public ILiveSensitiveWord disableById(Integer id, Boolean disabled);

	public void update(ILiveSensitiveWord bean);
}