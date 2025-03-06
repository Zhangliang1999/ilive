package com.bvRadio.iLive.core.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.security.BadCredentialsException;
import com.bvRadio.iLive.common.security.UsernameNotFoundException;
import com.bvRadio.iLive.common.security.encoder.PwdEncoder;
import com.bvRadio.iLive.core.dao.UnifiedUserDao;
import com.bvRadio.iLive.core.entity.UnifiedUser;
import com.bvRadio.iLive.core.manager.UnifiedUserMng;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;

@Service
@Transactional
public class UnifiedUserMngImpl implements UnifiedUserMng {

	public Integer errorRemaining(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			return null;
		}
		long now = System.currentTimeMillis();
		int maxErrorTimes = 3;
		int maxErrorInterval = 30 * 60 * 1000;
		Integer errorCount = user.getErrorCount();
		Date errorTime = user.getErrorTime();
		if (errorCount <= 0 || errorTime == null || errorTime.getTime() + maxErrorInterval < now) {
			return maxErrorTimes;
		}
		return maxErrorTimes - errorCount;
	}

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException {
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			user = getByMobile(username);
		}
		if (user == null) {
			throw new UsernameNotFoundException("username not found: " + username);
		}
		// if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
		if (!pwdEncoder.isPasswordValid(user.getPassword(), password, user.getSalt())) {
			updateLoginError(user.getId(), ip);
			throw new BadCredentialsException("password invalid");
		}
		if (!user.getActivation()) {
			// 用户需要激活才能进行登录
			// throw new BadCredentialsException("account not activated");
		}
		updateLoginSuccess(user.getId(), ip);
		return user;
	}

	/**
	 * 第三方登陆开发接口 只负责严重用户名
	 * 
	 * @param username
	 * @param ip
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws BadCredentialsException
	 */
	public UnifiedUser login(String username, String ip) throws UsernameNotFoundException, BadCredentialsException {
		UnifiedUser user = getByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username not found: " + username);
		}
		if (!user.getActivation()) {
			// throw new BadCredentialsException("account not activated");
		}
		updateLoginSuccess(user.getId(), ip);
		return user;
	}

	public void updateLoginSuccess(Integer userId, String ip) {
		UnifiedUser user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());

		user.setLoginCount(user.getLoginCount() + 1);
		user.setLastLoginIp(ip);
		user.setLastLoginTime(now);

		user.setErrorCount(0);
		user.setErrorTime(null);
		user.setErrorIp(null);
	}

	public void updateLoginError(Integer userId, String ip) {
		UnifiedUser user = findById(userId);
		Date now = new Timestamp(System.currentTimeMillis());
		int errorInterval = 30;
		Date errorTime = user.getErrorTime();

		user.setErrorIp(ip);
		if (errorTime == null || errorTime.getTime() + errorInterval * 60 * 1000 < now.getTime()) {
			user.setErrorTime(now);
			user.setErrorCount(1);
		} else {
			user.setErrorCount(user.getErrorCount() + 1);
		}
	}

	public boolean usernameExist(String username) {
		return getByUsername(username) != null;
	}

	public boolean emailExist(String email) {
		return dao.countByEmail(email) > 0;
	}

	public UnifiedUser getByUsername(String username) {
		return dao.getByUsername(username);
	}

	public List<UnifiedUser> getByEmail(String email) {
		return dao.getByEmail(email);
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public UnifiedUser findById(Integer id) {
		UnifiedUser entity = dao.findById(id);
		return entity;
	}

	public UnifiedUser ssoSave(UnifiedUser unifiedUser) {
		if (null != unifiedUser) {
			unifiedUser.init();
			dao.save(unifiedUser);
		}
		return unifiedUser;
	}

	public UnifiedUser save(String username, String email, String password, String ip) {
		long nextId = iLiveFieldIdManagerDao.getNextId("bbs_unified_user", "user_id", 1);
		UnifiedUser user = null;
		if (nextId != -1) {
			user = new UnifiedUser();
			user.setId((int) nextId);
			Date now = new Timestamp(System.currentTimeMillis());
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(pwdEncoder.encodePassword(password));
			user.setRegisterIp(ip);
			user.setRegisterTime(now);
			user.setLastLoginIp(ip);
			user.setLastLoginTime(now);
			// 不强制验证邮箱直接激活
			user.setActivation(true);
			user.init();
			dao.save(user);
		}
		return user;
	}

	public UnifiedUser save(String username, String email, String mobile, String password, String ip) {
		long nextId = iLiveFieldIdManagerDao.getNextId("bbs_unified_user", "user_id", 1);
		UnifiedUser user = null;
		if (nextId != -1) {
			Date now = new Timestamp(System.currentTimeMillis());
			user = new UnifiedUser();
			user.setId((int) nextId);
			user.setUsername(username);
			user.setMobile(mobile);
			if (!StringUtils.isBlank(mobile)) {
				user.setMobileAuth(UnifiedUser.IS_YES_AUTH);
			} else {
				user.setMobileAuth(UnifiedUser.IS_NO_AUTH);
			}
			user.setEmail(email);
			user.setEmailAuth(UnifiedUser.IS_NO_AUTH);
			// String salt = Common.getRandomString(6);
			String salt = "";
			user.setSalt(salt);
			password = pwdEncoder.encodePassword(password, salt);
			user.setPassword(password);
			user.setRegisterIp(ip);
			user.setRegisterTime(now);
			user.setLastLoginIp(ip);
			user.setLastLoginTime(now);
			// 不强制验证邮箱直接激活
			user.setActivation(true);
			user.init();
			dao.save(user);
		}
		return user;
	}

	public UnifiedUser save(String username, String email, String mobile, String thirdId, Integer thirdType,
			String password, String ip) {
		long nextId = iLiveFieldIdManagerDao.getNextId("bbs_unified_user", "user_id", 1);
		UnifiedUser user = null;
		if (nextId != -1) {
			Date now = new Timestamp(System.currentTimeMillis());
			user = new UnifiedUser();
			user.setId((int) nextId);
			user.setUsername(username);
			user.setMobile(mobile);
			if (!StringUtils.isBlank(mobile)) {
				user.setMobileAuth(UnifiedUser.IS_YES_AUTH);
			} else {
				user.setMobileAuth(UnifiedUser.IS_NO_AUTH);
			}
			user.setEmail(email);
			user.setEmailAuth(UnifiedUser.IS_NO_AUTH);
			// String salt = Common.getRandomString(6);
			String salt = "";
			user.setSalt(salt);
			password = pwdEncoder.encodePassword(password, salt);
			user.setPassword(password);
			user.setRegisterIp(ip);
			user.setRegisterTime(now);
			user.setLastLoginIp(ip);
			user.setLastLoginTime(now);
			// 不强制验证邮箱直接激活
			user.setActivation(true);
			user.setThirdId(thirdId);
			user.setThirdType(thirdType);
			user.init();
			dao.save(user);
		}
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, String)
	 */
	public UnifiedUser update(Integer id, String password, String email) {
		UnifiedUser user = findById(id);
		if (null != user) {
			if (!StringUtils.isBlank(email)) {
				user.setEmail(email);
			} else {
				user.setEmail(null);
			}
			if (!StringUtils.isBlank(password)) {
				user.setPassword(pwdEncoder.encodePassword(password, user.getSalt()));
			}
		}
		// redisDataCache.setReisForValue(UnifiedUser.REF+":"+id, user);
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, boolean)
	 */
	public UnifiedUser update(Integer id, String email, boolean auth) {
		UnifiedUser user = findById(id);
		if (!StringUtils.isBlank(email)) {
			user.setEmail(email);
		}
		if (auth) {
			user.setEmailAuth(UnifiedUser.IS_YES_AUTH);
		} else {
			user.setEmailAuth(UnifiedUser.IS_NO_AUTH);
		}
		// redisDataCache.setReisForValue(UnifiedUser.REF+":"+id, user);
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(UnifiedUser)
	 */
	public UnifiedUser update(UnifiedUser user) {
		Updater<UnifiedUser> updater = new Updater<UnifiedUser>(user);
		dao.updateByUpdater(updater);
		// redisDataCache.setReisForValue(UnifiedUser.REF+":"+user.getId(),
		// user);
		return user;
	}

	/**
	 * @see UnifiedUserMng#update(Integer, String, boolean)
	 */
	public UnifiedUser updateMobileValid(Integer id, String mobile, boolean isValid) {
		UnifiedUser user = findById(id);
		if (!StringUtils.isBlank(mobile)) {
			user.setMobile(mobile);
		} else {
			return null;
		}
		if (isValid) {
			user.setMobileAuth(UnifiedUser.IS_YES_AUTH);
		} else {
			user.setMobileAuth(UnifiedUser.IS_NO_AUTH);
		}

		// redisDataCache.setReisForValue(UnifiedUser.REF+":"+id, user);
		return user;
	}

	public boolean isPasswordValid(Integer id, String password) {
		UnifiedUser user = findById(id);
		String salt = user.getSalt();
		if (null == salt) {
			salt = "";
		}
		return pwdEncoder.isPasswordValid(user.getPassword(), password, salt);
	}

	public UnifiedUser deleteById(Integer id) {
		// UnifiedUser bean = dao.deleteById(id);
		UnifiedUser bean = dao.findById(id);
		bean.setUsername("");
		bean.setMobile("-1");
		bean.setThirdId(null);
		bean.setThirdType(null);
		return bean;
	}

	public UnifiedUser[] deleteByIds(Integer[] ids) {
		UnifiedUser[] beans = new UnifiedUser[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public UnifiedUser active(String username, String activationCode) {
		UnifiedUser bean = getByUsername(username);
		bean.setActivation(true);
		bean.setActivationCode(null);
		return bean;
	}

	public UnifiedUser activeLogin(UnifiedUser user, String ip) {
		updateLoginSuccess(user.getId(), ip);
		return user;
	}

	public int isMobileValid(String mobile) {
		return dao.countByMobile(mobile);
	}

	public UnifiedUser getByMobile(String mobile) {
		return dao.getByMobile(mobile);
	}

	public UnifiedUser getByThirdId(String thirdId) {
		return dao.getByThirdId(thirdId);
	}

	private PwdEncoder pwdEncoder;
	private UnifiedUserDao dao;
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Autowired
	public void setPwdEncoder(PwdEncoder pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
	}

	@Autowired
	public void setDao(UnifiedUserDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}

	@Override
	public UnifiedUser getByThirdIdAndType(String openId, int thirdTypeWeixin) {
		return dao.getByThirdIdAndType(openId, thirdTypeWeixin);
	}

	public ILiveFieldIdManagerDao getiLiveFieldIdManagerDao() {
		return iLiveFieldIdManagerDao;
	}

	@Autowired
	public void setiLiveFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}

}