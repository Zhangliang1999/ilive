package com.jwzt.billing.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.FunctionInfoDao;
import com.jwzt.billing.entity.FunctionInfo;
import com.jwzt.billing.manager.FunctionInfoMng;

/**
 * @author ysf
 */
@Service
public class FunctionInfoMngImpl implements FunctionInfoMng {

	@Override
	@Transactional(readOnly = true)
	public List<FunctionInfo> listByParams(Integer parentId, boolean initBean) {
		List<FunctionInfo> list = dao.listByParams(parentId);
		if (null != list && initBean) {
			for (FunctionInfo bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public FunctionInfo getBeanById(Integer id, boolean initBean) {
		FunctionInfo bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	private void initBean(final FunctionInfo bean) {
		if (null != bean) {
			Integer id = bean.getId();
			if (null != id) {
				List<FunctionInfo> childList = dao.listByParams(id);
				bean.setChildList(childList);
			}
		}
	}

	@Autowired
	private FunctionInfoDao dao;
}
