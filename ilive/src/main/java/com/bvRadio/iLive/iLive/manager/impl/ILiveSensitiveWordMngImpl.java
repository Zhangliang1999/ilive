package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveSensitiveWordDao;
import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSensitiveWordMng;
import com.bvRadio.iLive.common.page.Pagination;

@Service
@Transactional
public class ILiveSensitiveWordMngImpl implements ILiveSensitiveWordMng {
	@Transactional(readOnly = true)
	public Pagination getPage(String sensitiveName, Boolean disabled, int pageNo, int pageSize) {
		Pagination page = dao.getPage(sensitiveName, pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ILiveSensitiveWord findById(Integer id) {
		ILiveSensitiveWord entity = dao.findById(id);
		return entity;
	}

	public String replaceSensitiveWord(String origStr) {
		String replaceStr = origStr;
		List<ILiveSensitiveWord> sensitiveWordList = dao.getList(false);
		if (null != sensitiveWordList) {
			for (ILiveSensitiveWord sensitiveWord : sensitiveWordList) {
				replaceStr = sensitiveWord.replaceSensitiveWord(replaceStr);
			}
		}
		return replaceStr;
	}

	public ILiveSensitiveWord save(ILiveSensitiveWord bean) {
		long nextId = iLiveFieldIdManagerMng.getNextId("iLive_sensitive_word", "id", 1);
		if (nextId != -1) {
			bean.setId((int)nextId);
			bean.setCreateTime(new Timestamp(new Date().getTime()));
			// 默认启用
			bean.setDisabled(false);
			dao.save(bean);
		}
		return bean;
	}

	public ILiveSensitiveWord update(ILiveSensitiveWord bean) {
		if (null != bean && null != bean.getId()) {
			dao.update(bean);
		}
		return bean;
	}

	public ILiveSensitiveWord deleteById(Integer id) {
		ILiveSensitiveWord bean = dao.deleteById(id);
		return bean;
	}

	public ILiveSensitiveWord[] deleteByIds(Integer[] ids) {
		ILiveSensitiveWord[] beans = new ILiveSensitiveWord[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public ILiveSensitiveWord disableById(Integer id, Boolean disabled) {
		ILiveSensitiveWord bean = dao.disableById(id, disabled);
		return bean;
	}

	public ILiveSensitiveWord[] disableByIds(Integer[] ids, Boolean disabled) {
		ILiveSensitiveWord[] beans = new ILiveSensitiveWord[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = disableById(ids[i], disabled);
		}
		return beans;
	}

	@Autowired
	private ILiveSensitiveWordDao dao;
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	@Override
	public Pagination getPageByType(Integer type, Boolean disabled, int pageNo, int pageSize) {
		Pagination page = dao.getPageByType(type, pageNo, pageSize);
		return page;
	}

	@Override
	public Pagination getPageByTypeAndName(Integer type, String sensitiveName, Boolean disabled, int pageNo,
			int pageSize) {
		Pagination page = dao.getPageByTypeAndName(type,sensitiveName,pageNo, pageSize);
		return page;
	}

	@Override
	public List<ILiveSensitiveWord> getAllSentitiveword() {
		return dao.getAllSentitiveword();
	}
}
