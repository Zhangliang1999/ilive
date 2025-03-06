package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppSimpleCertInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.constants.ILiveCertStatus;
import com.bvRadio.iLive.iLive.entity.ILiveCertTopic;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.exception.ManagerExsitException;
import com.bvRadio.iLive.iLive.manager.ILiveCertTopicMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.realm.UsernamePasswordLoginTypeToken;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import sun.misc.BASE64Decoder;

@Controller
public class ILiveLoginAct {

	/**
	 * 日志记录
	 */
	private static final Logger log = LoggerFactory.getLogger(ILiveLoginAct.class);


	@Autowired
	private ILiveLiveRoomMng roomMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveManagerStateMng iLiveManagerStateMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng ILiveEenteriseMng;
	@Autowired
	private ILiveCertTopicMng iLiveCertTopicMng;
	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;
	
	// 混合校验
	private static final Integer mixCheck = 2;

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login(String logintype,HttpServletRequest request, HttpServletResponse response, ModelMap model, String token)
			throws IOException {
		if ("1".equals(ConfigUtils.get("redirectOpen"))) {
			String mpSystemAddr = ConfigUtils.get("mpSystemAddr");
			response.sendRedirect(mpSystemAddr);
			
			return null;
		} else {
			if(logintype==null) {
				logintype=0+"";
			}
			model.addAttribute("logintype", logintype);
			return "registered";
		}

	}

	@RequestMapping(value = "/registered.do", method = RequestMethod.GET)
	public String registeredPost(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			String token) {
		return "login";
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
				Integer enterpriseId = roomMng.initRoom(iLiveManager);
				roomMng.initMeet(iLiveManager, enterpriseId, request);
				UserBean userBean = ILiveUtils.getUser(request);
				userBean.setEnterpriseId(enterpriseId);
				ILiveManager iLiveManagerRefresh = iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
				user.setCertStatus(iLiveManagerRefresh.getCertStatus());
				String code=EnterpriseUtil.getEnterpriseMsg(user.getEnterpriseId(),user.getCertStatus());
				user.setFunctionCode(code);
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
	public String certInfo(HttpServletRequest request, Model model) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		if (iLiveEnterPrise.getCertStatus() == 0 || iLiveEnterPrise.getCertStatus() == 5) {
			Integer serverGroupId = this.selectServerGroup();
			model.addAttribute("serverGroupId", serverGroupId);
			return "enterpirse_cert/infoperson";
		} else {
			return "redirect:/admin/room/list.do";
		}
	}

	private Integer selectServerGroup() {
		return Integer.parseInt(ConfigUtils.get("defaultLiveServerGroupId"));
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
	public void registered(String username, String password, String vPassword,String terminalType, Integer validType,
			HttpServletResponse response,Integer type,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if (username == null) {
			json.put("status", "0");
			json.put("msg", "手机号不能为空");
		} else if (vPassword == null) {
			json.put("status", "0");
			json.put("msg", "验证码不能为空");
		} else {
			
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + username);
			String defaultVpassword = ConfigUtils.get("defaultVpassword");
			boolean ret = true;
			if (defaultVpassword.equals(vPassword)) {
				ret = true;
			} else if (cacheInfo == null) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确");
				ret = false;
			} else if (cacheInfo.isExpired()) {
				json.put("status", "0");
				json.put("msg", "输入的验证码已经过期,请重新获取");
				ret = false;
			} else if (!vPassword.equals(cacheInfo.getValue())) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确请重新获取验证码");
				CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + username);
				ret = false;
			}
			ILiveManager user1 = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
			//判断账户状态
			if(user1!=null) {
				Integer type1=user1.getSubType();
				if(type1==null) {
					type1=1;
				}
				
				
//				Long creattime=user1.getCreateTime().getTime();
//				Long newtime=System.currentTimeMillis();
//				Long time=newtime-creattime;
	             if(type1==0) {
					json.put("status", "0");
					json.put("msg", "账号已关闭，请联系管理员！");
					ret = false;
				}
//	             if((time/(1000*60*60*24))>30&&cer!=4) {
//	            	 	json.put("status", "0");
//						json.put("msg", "您的试用账号已到期，请完成企业认证。如需帮助，请联系客服118010。");
//						ret = false;
//	             }
			}
			
			if (ret) {
				try {
					ILiveManager mobile = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
					if (mobile == null) {
						// 1标记企业自服务注册
						try {
							long registeredManagerId = iLiveManagerMng.registeredManager(username, password, 1,terminalType);
							if (registeredManagerId > 0) {
								Subject subject = SecurityUtils.getSubject();
								UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username,
										"jwztadmin", false, null, "1", null, null);
								try {
									// 执行认证操作
									subject.login(utt);
									CacheManager.cacheExpired(cacheInfo);
									ILiveManager user = iLiveManagerMng.getILiveMangerByMobile(username);
									String ip = IPUtils.getRealIpAddr(request);
									//String region = IPUtils.getRegion(ip);
									user.setLastIP(ip);
									//user.setLastRegion(region);
									System.out.println("ip:"+ip);
									//更新最后一次登陆时间
									user.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
									iLiveManagerMng.updateLiveManager(user);
									net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(user);
									IPUtils.asyncSaveWork(user, workLogMng);
									if(cacheInfo!=null){
										cacheInfo.setExpired(true);
									}
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
							json.put("msg", "登录失败");
							e.printStackTrace();
						}
					} else {
						
						if(type!=null && type == 5){
							json.put("status", "0");
							json.put("msg", "号码已被注册");
							ResponseUtils.renderJson(response, json.toString());
							return ;
						}
						
						
						// 直接登录
						Subject subject = SecurityUtils.getSubject();
						UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, "jwztadmin",
								false, null, "1", null, null);
						try {
							// 执行认证操作.
							subject.login(utt);
							CacheManager.cacheExpired(cacheInfo);
							ILiveManager user = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
							Integer cer=user.getCertStatus();
							Integer enterpriseId=user.getEnterpriseId().intValue();
							System.out.println("enterpriseId========="+enterpriseId);
							Integer remainDays=EnterpriseUtil.checkValiteTime(enterpriseId, cer);
							user.setLastIP(IPUtils.getRealIpAddr(request));
							user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
							String ip = IPUtils.getRealIpAddr(request);
							user.setLastIP(ip);
							System.out.println("ip:"+ip);
							iLiveManagerMng.updateLiveManager(user);
							IPUtils.asyncSaveWork(user, workLogMng);
							if(cacheInfo!=null){
								cacheInfo.setExpired(true);
							}
							json.put("cer", cer);
							json.put("status", "1");
							json.put("remainDays", remainDays);
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
					json.put("msg", "登录失败,请稍后重试");
					e1.printStackTrace();
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
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
	@RequestMapping(value = "/zhxyregistered.do", method = RequestMethod.POST)
	public void zhxyregistered(String username, String password, String vPassword,String terminalType, Integer validType,String zhxyuserId,String appId,
			HttpServletResponse response,Integer type,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if (username == null) {
			json.put("status", "0");
			json.put("msg", "手机号不能为空");
		} else if (vPassword == null) {
			json.put("status", "0");
			json.put("msg", "验证码不能为空");
		} else {
			
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + username);
			String defaultVpassword = ConfigUtils.get("defaultVpassword");
			boolean ret = true;
			if (defaultVpassword.equals(vPassword)) {
				ret = true;
			} else if (cacheInfo == null) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确");
				ret = false;
			} else if (cacheInfo.isExpired()) {
				json.put("status", "0");
				json.put("msg", "输入的验证码已经过期,请重新获取");
				ret = false;
			} else if (!vPassword.equals(cacheInfo.getValue())) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确请重新获取验证码");
				CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + username);
				ret = false;
			}
			ILiveManager user1 = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
			//判断账户状态
			if(user1!=null) {
				Integer type1=user1.getSubType();
				if(type1==null) {
					type1=1;
				}
				
				
//				Long creattime=user1.getCreateTime().getTime();
//				Long newtime=System.currentTimeMillis();
//				Long time=newtime-creattime;
	             if(type1==0) {
					json.put("status", "0");
					json.put("msg", "账号已关闭，请联系管理员！");
					ret = false;
				}
//	             if((time/(1000*60*60*24))>30&&cer!=4) {
//	            	 	json.put("status", "0");
//						json.put("msg", "您的试用账号已到期，请完成企业认证。如需帮助，请联系客服118010。");
//						ret = false;
//	             }
			}
			
			if (ret) {
				try {
					ILiveManager mobile = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
					if (mobile == null) {
						// 1标记企业自服务注册
						try {
							long registeredManagerId = iLiveManagerMng.zhxyregisteredManager(username, password, 1,terminalType,zhxyuserId,appId);
							if (registeredManagerId > 0) {
								Subject subject = SecurityUtils.getSubject();
								UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username,
										"jwztadmin", false, null, "1", null, null);
								try {
									// 执行认证操作
									subject.login(utt);
									CacheManager.cacheExpired(cacheInfo);
									ILiveManager user = iLiveManagerMng.getILiveMangerByMobile(username);
									String ip = IPUtils.getRealIpAddr(request);
									//String region = IPUtils.getRegion(ip);
									user.setLastIP(ip);
									//user.setLastRegion(region);
									System.out.println("ip:"+ip);
									//更新最后一次登陆时间
									user.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
									iLiveManagerMng.updateLiveManager(user);
									net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(user);
									IPUtils.asyncSaveWork(user, workLogMng);
									if(cacheInfo!=null){
										cacheInfo.setExpired(true);
									}
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
							json.put("msg", "登录失败");
							e.printStackTrace();
						}
					} else {
						
						if(type!=null && type == 5){
							json.put("status", "0");
							json.put("msg", "号码已被注册");
							ResponseUtils.renderJson(response, json.toString());
							return ;
						}
						
						
						// 直接登录
						Subject subject = SecurityUtils.getSubject();
						UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, "jwztadmin",
								false, null, "1", null, null);
						try {
							// 执行认证操作.
							subject.login(utt);
							CacheManager.cacheExpired(cacheInfo);
							ILiveManager user = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
							Integer cer=user.getCertStatus();
							Integer enterpriseId=user.getEnterpriseId().intValue();
							System.out.println("enterpriseId========="+enterpriseId);
							Integer remainDays=EnterpriseUtil.checkValiteTime(enterpriseId, cer);
							user.setLastIP(IPUtils.getRealIpAddr(request));
							user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
							String ip = IPUtils.getRealIpAddr(request);
							user.setLastIP(ip);
							user.setZhxyappId(appId);
							user.setZhxyuserId(zhxyuserId);
							System.out.println("ip:"+ip);
							iLiveManagerMng.updateLiveManager(user);
							IPUtils.asyncSaveWork(user, workLogMng);
							if(cacheInfo!=null){
								cacheInfo.setExpired(true);
							}
							json.put("cer", cer);
							json.put("status", "1");
							json.put("remainDays", remainDays);
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
					json.put("msg", "登录失败,请稍后重试");
					e1.printStackTrace();
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	@RequestMapping(value = "/cert/state_monitor.do")
	public void getCertState(HttpServletRequest request, HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManagerState managerState = iLiveManagerStateMng
					.getIliveManagerState(Long.parseLong(user.getUserId()));
			if (managerState == null) {
				managerState = new ILiveManagerState();
				managerState.setCertStatus(user.getCertStatus());
				managerState.setManagerId(Long.parseLong(user.getUserId()));
				iLiveManagerStateMng.saveIliveManagerState(managerState);
				// ILiveManager iLiveManager =
				// iLiveManagerMng.getILiveManager(Long.parseLong(user.getUserId()));
				// ILiveEnterprise enterPrise = iLiveManager.getEnterPrise();
				// IliveAppRetInfo userInfo = this.buildAppRet(iLiveManager,
				// enterPrise);
				// AppUserStateVo userStateVo = new AppUserStateVo();
				user.setCertStatus(managerState.getCertStatus());
				String code=EnterpriseUtil.getEnterpriseMsg(user.getEnterpriseId(),user.getCertStatus());
				user.setFunctionCode(code);
				ILiveUtils.setUser(request, user);
				// userStateVo.setUpdateState(1);
				// userStateVo.setUserInfo(userInfo);
				// resultJson.put("code", SUCCESS_STATUS);
				// resultJson.put("message", "用户状态初始化");
				// resultJson.put("data", userStateVo);
			} else {
				if (managerState.getCertStatus().equals(user.getCertStatus())) {
					// 用户状态未发生改变
					// AppUserStateVo userStateVo = new AppUserStateVo();
					// userStateVo.setUpdateState(0);
					// resultJson.put("code", SUCCESS_STATUS);
					// resultJson.put("message", "用户状态未发生改变");
					// resultJson.put("data", userStateVo);
				} else {
					// // 用户状态发生了改变
					// ILiveManager iLiveManager =
					// iLiveManagerMng.getILiveManager(Long.parseLong(appUser.getUserId()));
					// ILiveEnterprise enterPrise =
					// iLiveManager.getEnterPrise();
					// IliveAppRetInfo userInfo = this.buildAppRet(iLiveManager,
					// enterPrise);
					// AppUserStateVo userStateVo = new AppUserStateVo();
					// userStateVo.setUpdateState(1);
					// userStateVo.setUserInfo(userInfo);
					// appUser.setCertStatus(managerState.getCertStatus());
					iLiveManagerStateMng.updateIliveManagerState(managerState);
					user.setCertStatus(managerState.getCertStatus());
					String code=EnterpriseUtil.getEnterpriseMsg(user.getEnterpriseId(),user.getCertStatus());
					user.setFunctionCode(code);
					ILiveUtils.setUser(request, user);
					// resultJson.put("message", "用户状态发生改变");
					// resultJson.put("data", userStateVo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultJson.put("code", SUCCESS_STATUS);
		ResponseUtils.renderJson(response, resultJson.toString());
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
					try {
						String code=EnterpriseUtil.getEnterpriseMsg(user.getEnterpriseId(),user.getCertStatus());
						user.setFunctionCode(code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ILiveUtils.setUser(request, user);
					cacheInfo.setExpired(true);
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
			HttpServletResponse response,HttpServletRequest request) {
		try{
			JSONObject json = new JSONObject();
			if (mixCheck.equals(validType)) {
				password=new String(new BASE64Decoder().decodeBuffer(password));
				Subject subject = SecurityUtils.getSubject();
				UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, password, false, null,
						"2", null, null);
				try {
					// 执行认证操作.
					subject.login(utt);
					//判断密码是否过期
					ILiveManager user = iLiveManagerMng.getILiveMangerByMobile(username);
					Integer cer=user.getCertStatus();
					Integer enterpriseId=user.getEnterpriseId().intValue();
					//判断账户是否过期
					Integer remainDays=EnterpriseUtil.checkValiteTime(enterpriseId, cer);
//					Long creattime=user.getCreateTime().getTime();
//					Long newtime=System.currentTimeMillis();
//					Long time=newtime-creattime;
					//判断账户状态
				
					Integer type=user.getSubType();
					if(type==null) {
						type=1;
					}
					if(remainDays==null) {
						remainDays=0;
					}
					
					if(type==0) {
						json.put("status", "0");
						json.put("msg", "账号已关闭，请联系管理员！");
					}
//						else if((time/(1000*60*60*24))>30&&cer!=4) {
//						json.put("status", "0");
//						json.put("msg", "您的试用账号已到期，请完成企业认证。如需帮助，请联系客服118010。");
//					}
					else
					if (validType==2) {
						Long now = new Date().getTime();
						if(user.getUpdatePasswordTime() == null) {
							user.setUpdatePasswordTime(user.getCreateTime());
							user.setLastIP(IPUtils.getRealIpAddr(request));
							iLiveManagerMng.updateLiveManager(user);
						}
						if((now - user.getUpdatePasswordTime().getTime())>(365*24*60*60*1000l)) {
							json.put("status", "0");
							json.put("msg", "密码已过期");
						}else {
							json.put("status", "1");
							json.put("remainDays", remainDays);
							json.put("msg", "登录成功");
							//更新最后一次登陆时间
							user.setLastLoginTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
							iLiveManagerMng.updateLiveManager(user);
							//记录日志
							workLogMng.save(new WorkLog(WorkLog.MODEL_LOGIN, user.getUserId()+"", net.sf.json.JSONObject.fromObject(user).toString(), WorkLog.MODEL_LOGIN_NAME, user.getUserId(), user.getMobile(), ""));
						}
					}else {
						json.put("status", "1");
						json.put("remainDays", remainDays);
						json.put("msg", "登录成功");
						workLogMng.save(new WorkLog(WorkLog.MODEL_LOGIN, user.getUserId()+"", net.sf.json.JSONObject.fromObject(user).toString(), WorkLog.MODEL_LOGIN_NAME, user.getUserId(), user.getMobile(), ""));
					}
					
					
				} catch (UnknownAccountException e) {
					json.put("status", "0");
					json.put("msg", "用户名或密码输入错误");
				} catch (IncorrectCredentialsException e) {
					ILiveManager user = iLiveManagerMng.getILiveMangerByMobile(username);
					if (user.getCalendar()==null) {
						user.setCalendar(Calendar.getInstance());
						user.setErrorNum(5);
					}
					Calendar calendar = user.getCalendar();
					Calendar now = Calendar.getInstance();
					if(calendar.YEAR==now.YEAR && calendar.MONTH == now.MONTH&&calendar.DAY_OF_MONTH==now.DAY_OF_MONTH) {
						if(user.getErrorNum()>1) {
							user.setErrorNum(user.getErrorNum()-1);
							json.put("msg", "用户名或密码输入错误");
						}else {
							user.setErrorNum(user.getErrorNum()-1);
							Calendar lockCalendar = Calendar.getInstance();
							lockCalendar.add(Calendar.DATE, 1);
							user.setLockCalendar(lockCalendar);
							json.put("msg", "您的账户被锁定,请明天再试。");
						}
					}else {
						user.setCalendar(now);
						user.setErrorNum(4);
						json.put("msg", "用户名或密码输入错误");
					}
					json.put("passworderror", "1");
					
					json.put("status", "0");
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 校验密码
	 * 
	 * @param password
	 * @param iLiveMangerByMobile
	 * @return
	 */
	// private boolean validPassWord(String password, ILiveManager
	// iLiveMangerByMobile) {
	// String salt = iLiveMangerByMobile.getSalt();
	// return false;
	// }

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
