package com.bvRadio.iLive.iLive.realm;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

public class SystemAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private ILiveManagerDao iLiveManagerDao;

	@Autowired
	private HttpServletRequest request;

	String password = null;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Object principal = principals.getPrimaryPrincipal();// 获取登录的用户名
		// // System.out.println(principal);

		/**
		 * 权限赋予
		 */
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		UsernamePasswordLoginTypeToken tt = (UsernamePasswordLoginTypeToken) token;
		SimpleAuthenticationInfo info = null;
		if (tt.getLoginType().equals("1")) {
			Object principal = token.getPrincipal();
			String credentials = "9885973ed115d27da79d7dc31589cf14";
			// 3.设置盐值 ，（加密的调料，让加密出来的东西更具安全性，一般是通过数据库查询出来的。
			// 简单的说，就是把密码根据特定的东西而进行动态加密，如果别人不知道你的盐值，就解不出你的密码）
			String source = "1687";
			ByteSource credentialsSalt = new Md5Hash(source);
			// 当前 Realm 的 name
			String realmName = getName();
			// 返回值实例化
			info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
			UserBean userBean = new UserBean();
			ILiveManager user = iLiveManagerDao.getILiveMangerByPhoneNumber(tt.getUsername());
			userBean.setUserId(String.valueOf(user.getUserId()));
			userBean.setUserThumbImg(user.getUserImg());
			userBean.setUsername(user.getNailName());
			userBean.setLevel(user.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:user.getLevel());
			userBean.setRoomId(user.getRoomId()==null?0:user.getRoomId());
			userBean.setCertStatus(user.getCertStatus() == null ? 0 : user.getCertStatus());
			ILiveEnterprise enterPrise = user.getEnterPrise();
			if (enterPrise != null) {
				userBean.setEnterpriseId(enterPrise.getEnterpriseId());
			} else {
				userBean.setEnterpriseId(10);
			}
			ILiveUtils.setUser(request, userBean);
		} else if(tt.getLoginType().equals("2")){
			// 1. token 中获取登录的 username! 注意不需要获取password.
			String invitationCode = tt.getUsername();
			// 2. 利用 username 查询数据库得到用户的信息
			ILiveManager user = iLiveManagerDao.getILiveMangerByMobile(invitationCode);
			
			//判断是否被自动锁定了
			java.util.Calendar nowDate = java.util.Calendar.getInstance();
			
			
			if (user != null) {
				password = user.getUserPwd();
			} else {
				throw new UnknownAccountException("用户不存在");
			}
			
			if(user.getLockCalendar()!= null && nowDate.compareTo(user.getLockCalendar())<0){
				throw new IncorrectCredentialsException("用户被锁定");
			}
			
			String credentials = password;
			// 3.设置盐值 ，（加密的调料，让加密出来的东西更具安全性，一般是通过数据库查询出来的。
			// 简单的说，就是把密码根据特定的东西而进行动态加密，如果别人不知道你的盐值，就解不出你的密码）
			String source = user.getSalt();
			ByteSource credentialsSalt = new Md5Hash(source);
			// 当前 Realm 的 name
			String realmName = getName();
			// 返回值实例化
			info = new SimpleAuthenticationInfo(invitationCode, credentials, credentialsSalt, realmName);
			UserBean userBean = new UserBean();
			userBean.setUserId(String.valueOf(user.getUserId()));
			userBean.setUserThumbImg(user.getUserImg());
			userBean.setUsername(user.getNailName());
			userBean.setLevel(user.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:user.getLevel());
			userBean.setRoomId(user.getRoomId()==null?0:user.getRoomId());
			userBean.setCertStatus(user.getCertStatus() == null ? 0 : user.getCertStatus());
			ILiveEnterprise enterPrise = user.getEnterPrise();
			if (enterPrise != null) {
				userBean.setEnterpriseId(enterPrise.getEnterpriseId());
			} else {
				userBean.setEnterpriseId(10);
			}
			ILiveUtils.setUser(request, userBean);			
		}else{
			// 1. token 中获取登录的 username! 注意不需要获取password.
			Object principal = token.getPrincipal();
			// 2. 利用 username 查询数据库得到用户的信息
			ILiveManager user = iLiveManagerDao.getILiveMangerByMobile((String) principal);
			if (user != null) {
				password = user.getUserPwd();
			} else {
				throw new UnknownAccountException("用户不存在");
			}
			String credentials = password;
			// 3.设置盐值 ，（加密的调料，让加密出来的东西更具安全性，一般是通过数据库查询出来的。
			// 简单的说，就是把密码根据特定的东西而进行动态加密，如果别人不知道你的盐值，就解不出你的密码）
			String source = user.getSalt();
			ByteSource credentialsSalt = new Md5Hash(source);
			// 当前 Realm 的 name
			String realmName = getName();
			// 返回值实例化
			info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
			UserBean userBean = new UserBean();
			userBean.setUserId(String.valueOf(user.getUserId()));
			userBean.setUserThumbImg(user.getUserImg());
			userBean.setUsername(user.getNailName());
			userBean.setLevel(user.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:user.getLevel());
			userBean.setRoomId(user.getRoomId()==null?0:user.getRoomId());
			userBean.setCertStatus(user.getCertStatus() == null ? 0 : user.getCertStatus());
			ILiveEnterprise enterPrise = user.getEnterPrise();
			if (enterPrise != null) {
				userBean.setEnterpriseId(enterPrise.getEnterpriseId());
			} else {
				userBean.setEnterpriseId(10);
			}
			ILiveUtils.setUser(request, userBean);
		}
		return info;
	}

	// init-method 配置.
	public void setCredentialMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");// MD5算法加密
		credentialsMatcher.setHashIterations(1024);// 1024次循环加密
		setCredentialsMatcher(credentialsMatcher);
	}

	// 用来测试的算出密码password盐值加密后的结果，下面方法用于新增用户添加到数据库操作的，我这里就直接用main获得，直接数据库添加了，省时间
	public static void main(String[] args) {
		String saltSource = "abcdef";
		String hashAlgorithmName = "MD5";
		String credentials = "123456";
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		 System.out.println(result);
	}

}
