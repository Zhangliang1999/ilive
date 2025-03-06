package com.bvRadio.iLive.manager.realm;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
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

import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.entity.IliveOperationUser;
import com.bvRadio.iLive.manager.manager.ILiveManagerOperatorUserMng;

public class SystemAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ILiveManagerOperatorUserMng iLiveManagerOperatorUserMng;

	String password = null;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Object principal = principals.getPrimaryPrincipal();// 获取登录的用户名

		/**
		 * 权限赋予
		 */
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		// 1. token 中获取登录的 username! 注意不需要获取password.
		Object principal = token.getPrincipal();
		// 2. 利用 username 查询数据库得到用户的信息
		IliveOperationUser user = iLiveManagerOperatorUserMng.getUserByUserName((String) principal);
		if (user != null) {
			password = user.getPassword();
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
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt,
				realmName);
		UserBean userBean = this.convertUser2WebVO(user);
		ILiveUtils.setUser(request, userBean);
		return info;
	}

	private UserBean convertUser2WebVO(IliveOperationUser user) {
		UserBean userBean = new UserBean();
		userBean.setUserId(String.valueOf(user.getUserId()));
		userBean.setUsername(user.getUserName());
		userBean.setUserThumbImg(user.getUserImg());
		return userBean;
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
		String saltSource = "1561";
		String hashAlgorithmName = "MD5";
		String credentials = "jwztadmin";
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		System.out.println(result);
	}

}
