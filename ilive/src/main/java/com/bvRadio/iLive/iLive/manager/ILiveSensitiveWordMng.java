package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveSensitiveWordMng {
	public Pagination getPage(String sensitiveName, Boolean disabled, int pageNo, int pageSize);

	//根据类型获取分页数据
	public Pagination getPageByType(Integer type, Boolean disabled, int pageNo, int pageSize);
	public Pagination getPageByTypeAndName(Integer type,String sensitiveName, Boolean disabled, int pageNo, int pageSize);
	
	public String replaceSensitiveWord(String origStr);

	public ILiveSensitiveWord findById(Integer id);

	public ILiveSensitiveWord save(ILiveSensitiveWord bean);

	public ILiveSensitiveWord update(ILiveSensitiveWord bean);

	public ILiveSensitiveWord deleteById(Integer id);

	public ILiveSensitiveWord[] deleteByIds(Integer[] ids);

	public ILiveSensitiveWord disableById(Integer id, Boolean disabled);

	public ILiveSensitiveWord[] disableByIds(Integer[] ids, Boolean disabled);
	
	//获取所有敏感词
	public List<ILiveSensitiveWord> getAllSentitiveword();

}