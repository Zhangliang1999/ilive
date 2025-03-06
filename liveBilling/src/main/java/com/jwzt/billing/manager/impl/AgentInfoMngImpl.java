package com.jwzt.billing.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.AgentInfoDao;
import com.jwzt.billing.entity.AgentInfo;
import com.jwzt.billing.manager.AgentInfoMng;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class AgentInfoMngImpl implements AgentInfoMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(String agentName, Integer status, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Pagination page = dao.pageByParams(agentName, status, startTime, endTime, pageNo, pageSize);
		if (null != page) {
			List<AgentInfo> list = (List<AgentInfo>) page.getList();
			if (null != list) {
				for (AgentInfo bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AgentInfo> listByParams(String agentName, Integer status, Date startTime, Date endTime) {
		List<AgentInfo> list = dao.listByParams(agentName, status, startTime, endTime);
		if (null != list) {
			for (AgentInfo bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public AgentInfo getBeanById(Integer id) {
		AgentInfo bean = dao.getBeanById(id);
		initBean(bean);
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AgentInfo save(final AgentInfo bean) {
		if (null != bean) {
			Long nextId = fieldIdManagerMng.getNextId("billing_agent", "id", 1L);
			if (null != nextId && nextId > 0) {
				int agentId = nextId.intValue();
				bean.setId(agentId);
				bean.initFieldValue();
				dao.save(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AgentInfo update(AgentInfo bean) {
		if (null != bean) {
			Updater<AgentInfo> updater = new Updater<AgentInfo>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	private void initBean(final AgentInfo bean) {
		if (null != bean) {
		}
	}

	@Autowired
	private AgentInfoDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
}
