package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.ILiveUploadServerDao;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.common.page.Pagination;

@Service
@Transactional
public class ILiveUploadServerMngImpl implements ILiveUploadServerMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<ILiveUploadServer> getList() {
		List<ILiveUploadServer> list = dao.getList();
		return list;
	}

	@Transactional(readOnly = true)
	public ILiveUploadServer findById(Integer id) {
		ILiveUploadServer entity = dao.findById(id);
		return entity;
	}

	@Transactional(readOnly = true)
	public ILiveUploadServer findDefaultSever() {
		ILiveUploadServer entity = dao.findDefaultSever();
		return entity;
	}

	public ILiveUploadServer save(ILiveUploadServer bean) {
		long nextId = iLiveFieldIdManagerDao.getNextId("ilive_upload_server", "id", 1);
		if (nextId != -1) {
			bean.setId((int)nextId);
			dao.save(bean);
		}
		return bean;
	}

	public ILiveUploadServer update(ILiveUploadServer bean) {
		if (null != bean && null != bean.getId()) {
			String ftpPassword = bean.getFtpPassword();
			if (StringUtils.isBlank(ftpPassword)) {
				ILiveUploadServer uploadServer = dao.findById(bean.getId());
				uploadServer.setFtpEncoding(bean.getFtpEncoding());
				uploadServer.setFtpIp(bean.getFtpIp());
				uploadServer.setFtpPath(bean.getFtpPath());
				uploadServer.setFtpPort(bean.getFtpPort());
				uploadServer.setFtpUsername(bean.getFtpUsername());
				uploadServer.setHttpMount(bean.getHttpMount());
				uploadServer.setHttpUrl(bean.getHttpUrl());
			} else {
				dao.update(bean);
			}
		}
		return bean;
	}

	public ILiveUploadServer deleteById(Integer id) {
		ILiveUploadServer bean = dao.deleteById(id);
		return bean;
	}

	public ILiveUploadServer[] deleteByIds(String[] ids) {
		ILiveUploadServer[] beans = new ILiveUploadServer[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i]= dao.deleteById(Integer.valueOf(ids[i]));
		}
		return beans;
	}

	private ILiveUploadServerDao dao;
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Autowired
	public void setDao(ILiveUploadServerDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}

	@Override
	public ILiveUploadServer getDefaultServer() {
		return dao.getDefaultServer();
	}
}
