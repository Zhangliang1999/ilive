package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseTerminalUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseTerminalUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
public class ILiveEnterpriseTerminalUserMngImpl implements ILiveEnterpriseTerminalUserMng {

	@Autowired
	private ILiveEnterpriseTerminalUserDao terminaluserDao;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(String queryNum, Integer fanstype,Integer pageNo, Integer pageSize, Integer enterpriseId) {
		return terminaluserDao.getPage(queryNum,fanstype, pageNo, pageSize,enterpriseId);
	}


	@Override
	@Transactional(readOnly = true)
	public Pagination getPageBlackList(Integer enterpriseId,String queryNum, Integer pageNo, Integer pageSize) {
		return terminaluserDao.getPageBlackList(enterpriseId,queryNum, pageNo, pageSize);
	}

	@Override
	@Transactional
	public ILiveEnterpriseTerminalUser saveTerminaluser(ILiveEnterpriseTerminalUser user) {
		Long userId = user.getUserId();
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterpriseTerminalUser terminalUser = terminaluserDao.getIliveEnterpriseTerminalByQuery(userId,
				enterpriseId);
		if (terminalUser != null) {
			terminalUser = this.convertVo2Po(terminalUser, user);
			terminaluserDao.updateIliveEnterpriseTerminalUser(terminalUser);
			return terminalUser;
		} else {
			Long nextId = filedIdMng.getNextId("ilive_enterprise_terminaluser", "id", 1);
			user.setId(nextId);
			terminaluserDao.saveTerminaluser(user);
			return user;
		}
	}

	/**
	 * 转换对象
	 * 
	 * @param terminalUser
	 * @param user
	 * @return
	 */
	private ILiveEnterpriseTerminalUser convertVo2Po(ILiveEnterpriseTerminalUser terminalUser,
			ILiveEnterpriseTerminalUser user) {
		terminalUser.setFansType(user.getFansType());
		terminalUser.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
		terminalUser.setNailName(user.getNailName());
		terminalUser.setLoginType(user.getLoginType());
		return terminalUser;
	}

	@Override
	public void letbuserblack(Long id) {
		terminaluserDao.letbuserblack(id);
	}

	@Override
	public void removeTerminaluser(Long id) {
		terminaluserDao.removeTerminaluser(id);
	}

	@Override
	public ILiveEnterpriseTerminalUser getTerminalUser(Long userId, Integer enterpriseId) {
		ILiveEnterpriseTerminalUser iliveEnterpriseTerminalByQuery = terminaluserDao.getIliveEnterpriseTerminalByQuery(userId, enterpriseId);
		return iliveEnterpriseTerminalByQuery;
	}

	@Override
	@Transactional
	public void updateTerminalUser(ILiveEnterpriseTerminalUser terminalUser) {
		terminaluserDao.updateIliveEnterpriseTerminalUser(terminalUser);
	}

	@Override
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype) {
		return terminaluserDao.queryList(queryNum, fanstype);
	}
	@Override
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype, Integer enterpriseId) {
		return terminaluserDao.queryList(queryNum, fanstype,enterpriseId);
	}


	@Override
	public void removeBlackuser(Long id) {
		// TODO Auto-generated method stub
		terminaluserDao.removeBlackuser(id);
	}

}
