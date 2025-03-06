package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppSimpleCertInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.exception.ManagerExsitException;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.realm.UsernamePasswordLoginTypeToken;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
public class ILiveLoginAct {

	private static final Logger log = LoggerFactory.getLogger(ILiveLoginAct.class);

	@Autowired
	private ILiveLiveRoomMng roomMng;

	// 混合校验
	private static final Integer mixCheck = 2;

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model, String token) {
		return "login";
	}

	@RequestMapping(value = "/registered.do", method = RequestMethod.GET)
	public String registeredPost(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			String token) {
		return "registered";
	}

	@RequestMapping(value = "/opentrial")
	public String openTrial() {
		return "tryuse";
	}

	@RequestMapping(value = "/tryuse.do")
	public String tryUseLiveRoom(HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer certStatus = user.getCertStatus();
		if (certStatus == 0) {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
			// 准备试用
			try {
				roomMng.initRoom(iLiveManager);
				ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
				user.setCertStatus(iLiveManagerRefresh.getCertStatus());
				ILiveUtils.setUser(request, user);
				return "redirect:/admin/room/list.do";
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/admin/room/list.do";
			}
		} else {
			return "redirect:/admin/room/list.do";
		}
	}

	@RequestMapping(value = "/cert/enterprise.do")
	public String certInfo() {
		return "enterpirse_cert/infos";
	}

	/**
	 * 基于手机号的注册
	 * 
	 * @param username
	 * @param password
	 * @param vPassword
	 * @param validType
	 * @param response
	 */
	@RequestMapping(value = "/registered.do", method = RequestMethod.POST)
	public void registered(String username, String password, String vPassword, Integer validType,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (username == null) {
			json.put("status", "0");
			json.put("msg", "手机号不能为空");
		} else if (vPassword == null) {
			json.put("status", "0");
			json.put("msg", "验证码不能为空");
		} else {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + username);
			if (cacheInfo == null) {
				json.put("status", "0");
				json.put("msg", "验证码不合法");
			} else if (cacheInfo.isExpired()) {
				json.put("status", "0");
				json.put("msg", "验证码已过期,请重新发送");
			} else if (!vPassword.equals(cacheInfo.getValue())) {
				json.put("status", "0");
				json.put("msg", "验证码校验不通过");
			} else {
				try {
					ILiveManager mobile = iLiveManagerMng.getILiveMangerByMobile(username);
					if (mobile == null) {
						// 1标记企业自服务注册
						try {
							long registeredManagerId = iLiveManagerMng.registeredManager(username, password, 1);
							if (registeredManagerId > 0) {
								Subject subject = SecurityUtils.getSubject();
								UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username,
										"jwztadmin", false, null, "1", null, null);
								try {
									// 执行认证操作
									subject.login(utt);
									CacheManager.cacheExpired(cacheInfo);
									json.put("status", "1");
									json.put("msg", "登录成功");
								} catch (UnknownAccountException e) {
									json.put("status", "0");
									json.put("msg", "用户不存在");
								} catch (IncorrectCredentialsException e) {
									json.put("status", "0");
									json.put("msg", "用户名/密码错误");
								} catch (ExcessiveAttemptsException e) {
									json.put("status", "0");
									json.put("msg", "登录失败多次，账户锁定10分钟");
								} catch (AuthenticationException e) {
									e.printStackTrace();
									// 其他错误，比如锁定，如果想单独处理请单独catch处理
									json.put("status", "0");
									json.put("msg", "登录失败");
								}
							}
						} catch (Exception e) {
							json.put("status", "0");
							json.put("msg", "注册失败");
							e.printStackTrace();
						}
					} else {
						// 直接登录
						Subject subject = SecurityUtils.getSubject();
						UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, "jwztadmin",
								false, null, "1", null, null);
						try {
							// 执行认证操作.
							subject.login(utt);
							CacheManager.cacheExpired(cacheInfo);
							json.put("status", "1");
							json.put("msg", "登录成功");
						} catch (UnknownAccountException e) {
							json.put("status", "0");
							json.put("msg", "用户不存在");
						} catch (IncorrectCredentialsException e) {
							json.put("status", "0");
							json.put("msg", "用户名/密码错误");
						} catch (ExcessiveAttemptsException e) {
							json.put("status", "0");
							json.put("msg", "登录失败多次，账户锁定10分钟");
						} catch (AuthenticationException e) {
							e.printStackTrace();
							// 其他错误，比如锁定，如果想单独处理请单独catch处理
							json.put("status", "0");
							json.put("msg", "登录失败");
						}
					}
				} catch (ManagerExsitException e) {
					e.printStackTrace();
					json.put("status", "0");
					json.put("msg", "用户已存在");
				} catch (Exception e1) {
					json.put("status", "0");
					json.put("msg", "注册失败,请稍后重试");
					e1.printStackTrace();
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "cert/info.do")
	public void accomplishInfo(AppSimpleCertInfo simpleCertInfo, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String phoneNum = simpleCertInfo.getPhoneNum();
		if (phoneNum == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getUserName() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户账号不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getVpassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户账号不能为空");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getPassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码未填写");
			resultJson.put("data", new JSONObject());
		} else if (simpleCertInfo.getConfirmPassword() == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "确认密码未填写");
			resultJson.put("data", new JSONObject());
		} else if (!simpleCertInfo.getPassword().equals(simpleCertInfo.getConfirmPassword())) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "两次密码不一致");
			resultJson.put("data", new JSONObject());
		} else {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "simplecert_" + phoneNum);
			if (cacheInfo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (cacheInfo.isExpired()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码已过期,请重新发送");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (!simpleCertInfo.getVpassword().equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager managerUser = iLiveManagerMng.getILiveManagerByUserName(simpleCertInfo.getUserName());
			if (managerUser == null) {
				ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
				if (iLiveMangerByMobile != null) {
					iLiveMangerByMobile.setUserName(simpleCertInfo.getUserName());
					iLiveMangerByMobile.setSimpleEnterpriseName(simpleCertInfo.getSimpleEnterpriseName());
					String password = md5Passwd(simpleCertInfo.getPassword(), iLiveMangerByMobile.getSalt());
					iLiveMangerByMobile.setUserPwd(password);
					iLiveMangerByMobile.setCertStatus(1);
					iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
					ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise);
					UserBean user = ILiveUtils.getUser(request);
					user.setCertStatus(1);
					ILiveUtils.setUser(request, user);
					resultJson.put("data", new JSONObject(retInfo));
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "用户信息完善成功");
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户不存在");
					resultJson.put("data", new JSONObject());
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户账号已被注册");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	private static final String hashAlgorithmName = "MD5";

	private String md5Passwd(String password, String salt) {
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);
		String retpassword = result.toString();
		return retpassword;
	}

	/**
	 * 构建成appRet 企业对象
	 * 
	 * @param iLiveMangerByMobile
	 * @param enterPrise
	 * @return
	 */
	private IliveAppRetInfo buildAppRet(ILiveManager iLiveMangerByMobile, ILiveEnterprise enterPrise) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(iLiveMangerByMobile.getUserId());
		retInfo.setMobile(iLiveMangerByMobile.getMobile());
		retInfo.setNailName(iLiveMangerByMobile.getNailName());
		retInfo.setJpushId(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getJpushId()));
		retInfo.setUserName(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getUserName()));
		retInfo.setCertStatus(iLiveMangerByMobile.getCertStatus() == null ? 0 : iLiveMangerByMobile.getCertStatus());
		retInfo.setPhoto(iLiveMangerByMobile.getUserImg() == null ? "" : iLiveMangerByMobile.getUserImg());
		if (enterPrise != null) {
			ILiveAppEnterprise enterprise = new ILiveAppEnterprise();
			enterprise.setCertStatus(enterPrise.getCertStatus());
			enterprise.setEnterpriseId(enterPrise.getEnterpriseId());
			enterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterPrise.getEnterpriseImg()));
			enterprise.setEnterpriseName(enterPrise.getEnterpriseName());
			enterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
			enterprise.setHomePageUrl(StringPatternUtil.convertEmpty(enterprise.getHomePageUrl()));
			retInfo.setEnterprise(enterprise);
		}
		return retInfo;
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public void loginPost(String username, String password, String vpassowrd, Integer validType,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (mixCheck.equals(validType)) {
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, password, false, null,
					"0", null, null);
			try {
				// 执行认证操作.
				subject.login(utt);
				json.put("status", "1");
				json.put("msg", "登录成功");
			} catch (UnknownAccountException e) {
				json.put("status", "0");
				json.put("msg", "用户不存在");
			} catch (IncorrectCredentialsException e) {
				json.put("status", "0");
				json.put("msg", "用户名/密码错误");
			} catch (ExcessiveAttemptsException e) {
				// TODO: handle exception
				json.put("status", "0");
				json.put("msg", "登录失败多次，账户锁定10分钟");
			} catch (AuthenticationException e) {
				e.printStackTrace();
				// 其他错误，比如锁定，如果想单独处理请单独catch处理
				json.put("status", "0");
				json.put("msg", "登录失败");
			}
		} else {
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	/**
	 * 校验密码
	 * 
	 * @param password
	 * @param iLiveMangerByMobile
	 * @return
	 */
	private boolean validPassWord(String password, ILiveManager iLiveMangerByMobile) {
		String salt = iLiveMangerByMobile.getSalt();
		return false;
	}

	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request, ModelMap model) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/admin/login.do";
	}

	@RequestMapping("/cert/info.do")
	public String certList(ModelMap modelMap) {
		Subject subject = SecurityUtils.getSubject();
		Serializable id = subject.getSession().getId();
		modelMap.addAttribute("jsessionid", id);
		return "real/real_enterprise";
	}

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

}
