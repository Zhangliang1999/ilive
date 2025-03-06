package com.bvRadio.iLive.core.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.security.BadCredentialsException;
import com.bvRadio.iLive.common.security.UsernameNotFoundException;
import com.bvRadio.iLive.core.entity.UnifiedUser;

public interface UnifiedUserMng {

	public Integer errorRemaining(String username);

	public UnifiedUser login(String username, String password, String ip)
			throws UsernameNotFoundException, BadCredentialsException;

	/**
	 * 第三方登陆认证开放接口
	 * 
	 * @param username
	 * @param ip
	 * @return
	 * @throws UsernameNotFoundException
	 * @throws BadCredentialsException
	 */
	public UnifiedUser login(String username, String ip) throws UsernameNotFoundException, BadCredentialsException;

	public boolean usernameExist(String username);

	public boolean emailExist(String email);

	public UnifiedUser getByUsername(String username);

	public List<UnifiedUser> getByEmail(String email);

	public Pagination getPage(int pageNo, int pageSize);

	public UnifiedUser findById(Integer id);

	public UnifiedUser ssoSave(UnifiedUser unifiedUser);

	public UnifiedUser save(String username, String email, String password, String ip);

	public UnifiedUser save(String username, String email, String mobile, String password, String ip);

	public UnifiedUser save(String username, String email, String mobile, String thirdId, Integer thirdType,
			String password, String ip);

	/**
	 * 修改邮箱和密码
	 * 
	 * @param id
	 *            用户ID
	 * @param password
	 *            未加密密码。如果为null或空串则不修改。
	 * @param email
	 *            电子邮箱。如果为空串则设置为null。
	 * @return
	 */
	public UnifiedUser update(Integer id, String password, String email);

	/**
	 * 修改邮箱认证
	 * 
	 * @param id
	 * @param email
	 * @param auth
	 * @return
	 */
	public UnifiedUser update(Integer id, String email, boolean auth);

	/**
	 * 修改整个用户信息
	 * 
	 * @param user
	 * @return
	 */
	public UnifiedUser update(UnifiedUser user);

	/**
	 * 修改手机认证
	 * 
	 * @param id
	 * @param mobile
	 * @param isValid
	 * @return
	 */
	public UnifiedUser updateMobileValid(Integer id, String mobile, boolean isValid);

	/**
	 * 密码是否正确
	 * 
	 * @param id
	 *            用户ID
	 * @param password
	 *            未加密密码
	 * @return
	 */
	public boolean isPasswordValid(Integer id, String password);

	public UnifiedUser deleteById(Integer id);

	public UnifiedUser[] deleteByIds(Integer[] ids);

	public UnifiedUser active(String username, String activationCode);

	public UnifiedUser activeLogin(UnifiedUser user, String ip);

	public int isMobileValid(String mobile);

	public UnifiedUser getByMobile(String mobile);

	public UnifiedUser getByThirdId(String thirdId);

	public UnifiedUser getByThirdIdAndType(String openId, int thirdType);
}