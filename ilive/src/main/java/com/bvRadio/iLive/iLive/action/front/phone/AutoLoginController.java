package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;

import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.entity.AutoLogin;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.exception.FormatException;
import com.bvRadio.iLive.iLive.manager.AutoLoginService;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/autoLogin")
public class AutoLoginController {
	
	public static final String USER_SIGN_USERID = "_user_sign_userid";
	public static final String USER_SIGN_USERIMG = "_user_sign_userimg";
	
	@Autowired
	private AutoLoginService autoLoginService;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	private static Logger logger = LoggerFactory.getLogger(AutoLoginController.class);

	/**
	 * 统一登录接口
	 * @param appId
	 * @param token
	 * @param time
	 * @param callbacktoken
	 * @param roomId
	 * @param fileId
	 * @param mobile
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="login.jspx")
	public void login(String appId,String token,String time,String callbacktoken,Integer roomId,Integer fileId,
			String mobile,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			logger.info("开始验证");
			AutoLogin autoLogin = autoLoginService.getByAppId(appId);
			
			if (autoLogin!=null && autoLogin.getSecret()!=null) {
				String secret = autoLogin.getSecret();
				logger.info("secret验证完成");
				autoLogin.getSecret();
				String myToken = DigestUtils.md5Hex(time+"&"+secret+"&");
				if (myToken.equals(token)) {
					logger.info("token验证完成");
					
					if (true) {
						String formal_play_address = ConfigUtils.get("formal_play_address");
						String phoneNum = mobile;
						//手机号存在并且不为空
						if (phoneNum!=null&&!phoneNum.equals("")) {
							if (!isMobile(mobile)) {
								throw new FormatException();
							}
							logger.info("开始登陆");
							//登录
							ILiveManager iLiveManger = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
							if (iLiveManger != null) {
								logger.info("当前用户存在，开始登陆");
								ILiveEnterprise enterPrise = iLiveManger.getEnterPrise();
								IliveAppRetInfo retInfo = this.buildAppRet(iLiveManger, enterPrise , request);
								String loginToken = this.buildLoginToken();
								retInfo.setLoginToken(loginToken);
								iLiveManger.setLoginToken(loginToken);
								iLiveManagerMng.updateLiveManager(iLiveManger);
								retInfo.setJpushId(iLiveManger.getJpushId());
								UserBean userBean = new UserBean();
								userBean.setUserId(String.valueOf(iLiveManger.getUserId()));
								userBean.setLoginToken(loginToken);
								userBean.setNickname(iLiveManger.getNailName());
								userBean.setUserThumbImg(iLiveManger.getUserImg());
								userBean.setUsername(iLiveManger.getMobile());
								userBean.setCertStatus(iLiveManger.getCertStatus());
								userBean.setLevel(iLiveManger.getLevel()==null?0:iLiveManger.getLevel());
								logger.info("设置appUser");
								ILiveUtils.setAppUser(request, userBean);
								logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
							} else {
								// 0标识手机号注册
								try {
									logger.info("当前用户不存在，开始注册并登陆");
									long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, null, 0, 2,
											null);
									ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
									ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
									IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise , request);
									String loginToken = this.buildLoginToken();
									retInfo.setLoginToken(loginToken);
									iLiveMangerByMobile.setLoginToken(loginToken);
									iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
									retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
									UserBean userBean = new UserBean();
									userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
									userBean.setLoginToken(loginToken);
									userBean.setUsername(iLiveMangerByMobile.getMobile());
									userBean.setNickname(iLiveMangerByMobile.getNailName());
									userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
									userBean.setCertStatus(iLiveMangerByMobile.getCertStatus());
									logger.info("设置appUser");
									ILiveUtils.setAppUser(request, userBean);
									logger.info("reg登录时返回的sessionId->" + request.getSession().getId());
								} catch (Exception e) {
									e.printStackTrace();
									result.put("code", ERROR_STATUS);
									result.put("message", "注册时发生异常");
									result.put("data", new JSONObject());
								}
							}
							
							logger.info("进行页面跳转");
						}else {
							//手机号不存在，使用userID和appID登录
							long userId = 102l;
							ILiveManager manager = iLiveManagerMng.getILiveManager(userId);
							if (manager != null) {
								// 直接登录
								UserBean userBean = new UserBean();
								userBean.setUserId(String.valueOf(manager.getUserId()));
								userBean.setUsername(manager.getMobile());
								userBean.setNickname(manager.getNailName());
								userBean.setUserThumbImg(manager.getUserImg());
								userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
								userBean.setCertStatus(manager.getCertStatus());
								ILiveUtils.setAppUser(request, userBean);
							} else {
								result.put("code", AutoLogin.CODE_ERROR);
								result.put("msg", "userId不存在");
							}
							
						}
						if (fileId!=null) {
							response.sendRedirect(formal_play_address+"/pc/review.html?fileId="+fileId+"&roomId="+roomId+"");
						}else if (roomId!=null) {
							response.sendRedirect(formal_play_address+"/pc/play.html?roomId="+roomId+"");
						}else {
							response.sendRedirect(formal_play_address+"/home/index.html?enterpriseId=489&from=singlemessage");
						}
					}
					result.put("code", AutoLogin.CODE_SUCCESS);
					result.put("msg", "登录成功");
				}else {
					result.put("code", AutoLogin.CODE_ERROR);
					result.put("msg", "验证失败，token验证不通过");
				}
			}else {
				result.put("code", AutoLogin.CODE_ERROR);
				result.put("msg", "验证失败，secret不存在");
			}
		}catch(FormatException e) {
			e.printStackTrace();
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("msg", "手机号格式错误");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("msg", "登录失败,远程地址链接失败");
		}finally {
			ResponseUtils.renderJson(request, response, result.toString());
		}
	}
	
	/**
	 * 统一登录接口对接电信掌上营业厅
	 * @param appId
	 * @param token
	 * @param time
	 * @param callbacktoken
	 * @param roomId
	 * @param fileId
	 * @param mobile
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="loginapp.jspx")
	public void login2(String appId,String token,String time,String callbacktoken,Integer roomId,Integer fileId,
			String mobile,String userId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			logger.info("开始验证");
			AutoLogin autoLogin = autoLoginService.getByAppId(appId);
			
			if (autoLogin!=null && autoLogin.getSecret()!=null) {
				String secret = autoLogin.getSecret();
				logger.info("secret验证完成");
				autoLogin.getSecret();
				String myToken = DigestUtils.md5Hex(time+"&"+secret+"&");
				if (myToken.equals(token)) {
					logger.info("token验证完成");
					/*String url = ConfigUtils.get("login_zhangshangapp");
					Map<String, String> map = new HashMap<>();
					map.put("callbacktoken",callbacktoken );
					String sendPost = HttpUtils.sendPost(url, map, "UTF-8");*/
					if (true) {
						System.out.println("获取配置文件地址");
						String formal_play_address = ConfigUtils.get("formal_play_address");
						if (true) {
							System.out.println("解码userId");
							String aTOm = Base64EncoderUtil.aTOm(userId);
							userId = aTOm.split(",")[0];
							String nailName = ""+userId;
							String userName = ""+userId;
							String photo = ""+userId;
							ILiveManager manager = null;
							System.out.println("根据userid获取manager");
							try {
								manager = iLiveManagerMng.getILiveManagerByDXID(userId);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("根据userid获取manager出现异常");
							}
							
							System.out.println("获取manager");
							if (manager != null) {
								System.out.println("直接登录");
								
								// 直接登录
								UserBean userBean = new UserBean();
								userBean.setUserId(String.valueOf(manager.getUserId()));
								userBean.setUsername(manager.getMobile());
								userBean.setNickname(manager.getNailName());
								userBean.setUserThumbImg(manager.getUserImg());
								userBean.setEnterpriseId(manager.getEnterPrise().getEnterpriseId());
								userBean.setCertStatus(manager.getCertStatus());
								ILiveUtils.setAppUser(request, userBean);
							} else {
								System.out.println("注册登录");
								manager = new ILiveManager();
								manager.setDxzyappId(userId);
								manager.setNailName(nailName);
								manager.setUserName(userName);
								manager.setUserImg(photo);
								ILiveManager savedxapp = iLiveManagerMng.savedxapp(manager);
								UserBean userBean = new UserBean();
								userBean.setUserId(String.valueOf(savedxapp.getUserId()));
								userBean.setUsername(savedxapp.getMobile());
								userBean.setNickname(savedxapp.getNailName());
								userBean.setUserThumbImg(savedxapp.getUserImg());
								userBean.setEnterpriseId(savedxapp.getEnterPrise().getEnterpriseId());
								userBean.setCertStatus(savedxapp.getCertStatus());
								ILiveUtils.setAppUser(request, userBean);
							}
							
						}
						System.out.println("准备页面跳转");
						if (fileId!=null) {
							System.out.println("跳转1");
							response.sendRedirect(formal_play_address+"/pc/review.html?fileId="+fileId+"&roomId="+roomId+"");
						}else if (roomId!=null) {
							System.out.println("跳转2");
							response.sendRedirect(formal_play_address+"/pc/play.html?roomId="+roomId+"");
						}else {
							System.out.println("跳转3");
							response.sendRedirect(formal_play_address+"/home/index.html?enterpriseId=100&from=singlemessage");
						}
					}
					result.put("code", AutoLogin.CODE_SUCCESS);
					result.put("msg", "登录成功");
				}else {
					result.put("code", AutoLogin.CODE_ERROR);
					result.put("msg", "验证失败，token验证不通过");
				}
			}else {
				result.put("code", AutoLogin.CODE_ERROR);
				result.put("msg", "验证失败，secret不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("msg", "登录失败,远程地址链接失败");
		}finally {
			System.out.println("返回信息");
			ResponseUtils.renderJson(request, response, result.toString());
		}
	}
	
	/**
	 * 构建成appRet 企业对象
	 * 
	 * @param iLiveMangerByMobile
	 * @param enterPrise
	 * @return
	 */
	private IliveAppRetInfo buildAppRet(ILiveManager iLiveMangerByMobile, ILiveEnterprise enterPrise , HttpServletRequest request ) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(iLiveMangerByMobile.getUserId());
		retInfo.setMobile(iLiveMangerByMobile.getMobile());
		retInfo.setNailName(iLiveMangerByMobile.getNailName());
		retInfo.setJpushId(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getJpushId()));
		retInfo.setLoginToken(iLiveMangerByMobile.getLoginToken());
		retInfo.setUserName(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getUserName()));
		retInfo.setCertStatus(iLiveMangerByMobile.getCertStatus() == null ? 0 : iLiveMangerByMobile.getCertStatus());
		retInfo.setPhoto(iLiveMangerByMobile.getUserImg() == null ? "" : iLiveMangerByMobile.getUserImg());
		String ipAddress = request.getHeader("x-forwarded-for");
	     
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
		ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
		ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteHost();
		}
		if(ipAddress.indexOf(":")>0){
			retInfo.setIsIpV6(1);
		}else{
			retInfo.setIsIpV6(0);
		}
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
	
	/**
	 * 构建登录token
	 * 
	 * @return
	 */
	private String buildLoginToken() {
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		return token;
	}
	
	private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" +
            "|(" +
                "((13([0-3]|[5-9]))" +
                    "|149" +
                    "|15([0-3]|[5-9])" +
                    "|166" +
                    "|17(3|[5-8])" +
                    "|18[0-9]" +
                    "|19[8-9]" +
                ")" +
            "\\d{8}" +
            ")";
	private static boolean isMobile(String tel) {
		return Pattern.matches(REGEX_MOBILE, tel);
	}
	
	
	/**
	 * 统一登录接口
	 * @param appId
	 * @param token
	 * @param time
	 * @param callbacktoken
	 * @param roomId
	 * @param fileId
	 * @param mobile
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="loginapptw.jspx")
	public void loginapptw(String token,String time,Long userId,Integer roomId,Integer fileId,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			logger.info("开始验证");
			if (true) {
				String md5Hex = DigestUtils.md5Hex(time+"&tv189&"+userId.toString());
				if (md5Hex.equals(token)) {
					logger.info("token验证完成");
					if (true) {
						String local_host = ConfigUtils.get("local_host");
						//根据userId获取manager
						ILiveManager manager = iLiveManagerMng.getILiveManager(userId);
						
						if (manager==null) {
							result.put("code", AutoLogin.CODE_ERROR);
							result.put("msg", "该账号不存在");
						}else if (manager.getCertStatus()!=null&&manager.getCertStatus()==0) {
							//去认证界面
							System.out.println("跳转去认证界面");
							response.sendRedirect(local_host+"/ilive/admin/cert/cert.do?userId="+userId+"");
						}else {
							result.put("code", AutoLogin.CODE_SUCCESS);
							result.put("msg", "该账号已认证");
						}
							
					}
				}else {
					result.put("code", AutoLogin.CODE_ERROR);
					result.put("msg", "验证失败，token验证不通过");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("msg", "操作失败,远程地址链接失败");
		}finally {
			ResponseUtils.renderJson(request, response, result.toString());
		}
	}
	
}
class Base64EncoderUtil {
    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        }
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }

    // 加密
    public static String mTOa(Object ming) {
        return Base64EncoderUtil.getBASE64(Base64EncoderUtil.getBASE64(Base64EncoderUtil.getBASE64((String) ming)));
    }

    // 解密
    public static String aTOm(String an) {
        return Base64EncoderUtil.getFromBASE64(Base64EncoderUtil.getFromBASE64(Base64EncoderUtil.getFromBASE64(an)));
    }

    // 将 BASE64 编码的字符串 s进行解码解密
    public static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
    
    
}
