package com.bvRadio.iLive.core.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.security.BadCredentialsException;
import com.bvRadio.iLive.common.security.UsernameNotFoundException;
import com.bvRadio.iLive.common.web.session.SessionProvider;
import com.bvRadio.iLive.core.entity.Authentication;
import com.bvRadio.iLive.core.entity.UnifiedUser;

/**
 * 认证信息管理接口
 */
public interface AuthenticationMng {
	/**
	 * 认证信息session key
	 */
	public static final String AUTH_KEY = "auth_key";

	public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request);

	public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request, String authId);

	public void storeAuthIdToSession(SessionProvider session, HttpServletRequest request, HttpServletResponse response,
			String authId);

	/**
	 * 通过认证ID，获得认证信息。本方法会检查认证是否过期。
	 * 
	 * @param authId
	 *            认证ID
	 * @return 返回Authentication对象。如果authId不存在或已经过期，则返回null。
	 */
	public Authentication retrieve(String authId);

	/**
	 * 登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param ip
	 *            登录IP
	 * @return 认证信息
	 * @throws UsernameNotFoundException
	 *             用户名没有找到
	 * @throws BadCredentialsException
	 *             错误的认证信息，比如密码错误
	 */
	public Authentication login(String username, String password, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session)
			throws UsernameNotFoundException, BadCredentialsException;

	/**
	 * 第三方认证开放登陆接口(QQ,WEIXIN,WEIBO)
	 * 
	 * @param username
	 * @param ip
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws BadCredentialsException
	 */
	public Authentication login(String username, String ip, HttpServletRequest request, HttpServletResponse response,
			SessionProvider session) throws UsernameNotFoundException, BadCredentialsException;

	/**
	 * sso统一登录
	 * 
	 * @param userId
	 * @param username
	 * @param email
	 * @param ip
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public Authentication ssoLogin(Integer userId, String username, String email, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session);

	public List<Authentication> findListByUser(Integer userId);

	/**
	 * 注册后登录
	 * 
	 * @param user
	 * @param ip
	 *            登录IP
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public Authentication activeLogin(UnifiedUser user, String ip, HttpServletRequest request,
			HttpServletResponse response, SessionProvider session);

	/**
	 * 获得认证分页信息
	 * 
	 * @param pageNo
	 *            当前页数
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Pagination getPage(int pageNo, int pageSize);

	/**
	 * 根据认证ID查找认证信息
	 * 
	 * @param id
	 *            认证ID
	 * @return
	 */
	public Authentication findById(String id);

	/**
	 * 保存认证信息
	 * 
	 * @param bean
	 * @return
	 */
	public Authentication save(Authentication bean);

	/**
	 * 删除认证信息
	 * 
	 * @param id
	 * @return
	 */
	public Authentication deleteById(String id);

	/**
	 * 删除认证信息
	 * 
	 * @param ids
	 * @return
	 */
	public Authentication[] deleteByIds(String[] ids);

}