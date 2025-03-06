package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.AssistentLoginDao;
import com.bvRadio.iLive.iLive.entity.AssistentLogin;
import com.bvRadio.iLive.iLive.manager.AssistentLoginMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
@Service
public class AssistentLoginMngImpl implements AssistentLoginMng {
	@Autowired
	private AssistentLoginDao assistentLoginDao;
	@Autowired
	private ILiveFieldIdManagerMng fieldIdManagerMng;
	@Override
	@Transactional
	public void addAssistentLogin(AssistentLogin assistentLogin)
			throws Exception {
		Long nextId = fieldIdManagerMng.getNextId("ilive_assistent_login", "id", 1);
		assistentLogin.setId(nextId);
		assistentLoginDao.addAssistentLogin(assistentLogin);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination selectAssistentLoginByPage(Integer roomId,Integer pageNo,
			Integer pageSize) throws Exception {
		return assistentLoginDao.selectAssistentLoginByPage(roomId,pageNo, pageSize);
	}

}
