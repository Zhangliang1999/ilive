package com.bvRadio.iLive.core.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.security.BadCredentialsException;
import com.bvRadio.iLive.common.security.UsernameNotFoundException;
import com.bvRadio.iLive.common.web.session.SessionProvider;
import com.bvRadio.iLive.core.dao.AuthenticationDao;
import com.bvRadio.iLive.core.entity.Authentication;
import com.bvRadio.iLive.core.entity.UnifiedUser;
import com.bvRadio.iLive.core.manager.AuthenticationMng;
import com.bvRadio.iLive.core.manager.UnifiedUserMng;
import com.bvRadio.iLive.iLive.util.Common;

@Service
@Transactional
public class AuthenticationMngImpl implements AuthenticationMng {
	private Logger log = LoggerFactory.getLogger(AuthenticationMngImpl.class);

	public Authentication login(String username, String password, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session)
			throws UsernameNotFoundException, BadCredentialsException {
		UnifiedUser user = unifiedUserMng.login(username, password, ip);
		Authentication auth = new Authentication();
		auth.setUid(user.getId());
		auth.setUsername(user.getUsername());
		auth.setEmail(user.getEmail());
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		return auth;
	}

	public List<Authentication> findListByUser(Integer userId) {
		List<Authentication> auths = (List<Authentication>) dao.getListByUserId(userId);
		return auths;
	}

	public Authentication login(String username, String ip, HttpServletRequest request, HttpServletResponse response,
			SessionProvider session) throws UsernameNotFoundException, BadCredentialsException {
		UnifiedUser user = unifiedUserMng.login(username, ip);
		Authentication auth = new Authentication();
		auth.setUid(user.getId());
		auth.setUsername(user.getUsername());
		auth.setEmail(user.getEmail());
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		return auth;
	}

	public Authentication ssoLogin(Integer userId, String username, String email, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session) {
		Authentication auth = new Authentication();
		auth.setUid(userId);
		auth.setUsername(username);
		auth.setEmail(email);
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		return auth;
	}

	public Authentication activeLogin(UnifiedUser user, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session) {
		unifiedUserMng.activeLogin(user, ip);
		Authentication auth = new Authentication();
		auth.setUid(user.getId());
		auth.setUsername(user.getUsername());
		auth.setEmail(user.getEmail());
		auth.setLoginIp(ip);
		save(auth);
		session.setAttribute(request, response, AUTH_KEY, auth.getId());
		return auth;
	}

	public Authentication retrieve(String authId) {
		long current = System.currentTimeMillis();
		// 是否刷新数据库
		if (refreshTime < current) {
			refreshTime = getNextRefreshTime(current, interval);
			int count = dao.deleteExpire(new Date(current - timeout));
			log.info("refresh Authentication, delete count: {}", count);
		}
		Authentication auth = findById(authId);
		if (auth != null && auth.getUpdateTime().getTime() + timeout > current) {
			auth.setUpdateTime(new Timestamp(current));
			// redisDataCache.setReisForValue(auth.REF + ":" + auth.getId(),
			// auth);
			return auth;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	public Authentication ssoretrieve(String authId) {
		long current = System.currentTimeMillis();
		// 是否刷新数据库
		if (refreshTime < current) {
			refreshTime = getNextRefreshTime(current, interval);
			int count = dao.deleteExpire(new Date(current - timeout));
			// log.info("refresh Authentication, delete count: {}", count);
		}
		Authentication auth = findById(authId);
		if (auth != null && auth.getUpdateTime().getTime() + timeout > current) {
			auth.setUpdateTime(new Timestamp(current));
			return auth;
		} else if (auth != null && auth.getUpdateTime().getTime() + sso_refreshtime > current) {
			auth.setUpdateTime(new Timestamp(current));
			return auth;

		} else {
			if (auth != null) {
				return auth;
			} else {
				return null;
			}
		}

	}

	public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request) {
		String authId = (String) session.getAttribute(request, AUTH_KEY);
		if (authId == null) {
			return null;
		}
		Authentication auth = retrieve(authId);
		if (auth == null) {
			return null;
		}
		return auth.getUid();
	}

	public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request, String authId) {

		if (authId == null) {
			return null;
		}
		Authentication auth = ssoretrieve(authId);
		if (auth == null) {
			return null;
		}
		return auth.getUid();
	}

	public void storeAuthIdToSession(SessionProvider session, HttpServletRequest request, HttpServletResponse response,
			String authId) {
		session.setAttribute(request, response, AUTH_KEY, authId);
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public Authentication findById(String id) {
		Authentication entity = dao.findById(id);
		return entity;
	}

	public Authentication save(Authentication bean, UnifiedUser user) {
		String auth = Common.authCode(user.getPassword() + "\t" + user.getId(), "ENCODE", null, 0);
		// bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
		bean.setId(auth);
		bean.init();
		dao.save(bean);
		return bean;
	}

	public Authentication save(Authentication bean) {
		if (bean.getId() == null || bean.getId() == "") {
			bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
		}
		bean.init();
		dao.save(bean);
		return bean;
	}

	/**
	 * public Authentication save(Authentication bean) {
	 * bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
	 * bean.init(); dao.save(bean); return bean; }
	 **/
	public Authentication deleteById(String id) {
		Authentication bean = dao.deleteById(id);
		return bean;
	}

	public Authentication[] deleteByIds(String[] ids) {
		Authentication[] beans = new Authentication[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	// 过期时间
	private int timeout = 7 * 24 * 60 * 60 * 1000; // 7*24*60分钟

	// sso刷新时间
	private int sso_refreshtime = 7 * 24 * 60 * 60 * 1000; // 7*24*60分钟

	// 间隔时间
	private int interval = 7 * 24 * 60 * 60 * 1000; // 7*24小时

	// 刷新时间。
	private long refreshTime = getNextRefreshTime(System.currentTimeMillis(), this.interval);

	private UnifiedUserMng unifiedUserMng;
	private AuthenticationDao dao;

	@Autowired
	public void setDao(AuthenticationDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}

	/**
	 * 设置认证过期时间。默认30分钟。
	 * 
	 * @param timeout
	 *            单位分钟
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout * 60 * 1000;
	}

	/**
	 * 设置刷新数据库时间。默认4小时。
	 * 
	 * @param interval
	 *            单位分钟
	 */
	public void setInterval(int interval) {
		this.interval = interval * 60 * 1000;
		this.refreshTime = getNextRefreshTime(System.currentTimeMillis(), this.interval);
	}

	/**
	 * 获得下一个刷新时间。
	 * 
	 * 
	 * 
	 * @param current
	 * @param interval
	 * @return 随机间隔时间
	 */
	private long getNextRefreshTime(long current, int interval) {
		return current + interval;
		// 为了防止多个应用同时刷新，间隔时间=interval+RandomUtils.nextInt(interval/4);
		// return current + interval + RandomUtils.nextInt(interval / 4);
	}

}