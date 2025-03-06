package com.bvRadio.iLive.manager.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

import sun.misc.BASE64Decoder;

@Controller
public class IliveManagerLoginAct {

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model, String token) {
		return "login";
	}

	// 混合校验
	private static final Integer mixCheck = 2;

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public void loginPost(String username, String password, String vpassowrd, Integer validType,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			if (mixCheck.equals(validType)) {
				Subject subject = SecurityUtils.getSubject();
//				UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(username, password, false, null,
//						"0", null, null);

				password=new String(new BASE64Decoder().decodeBuffer(password));;
				UsernamePasswordToken ut = new UsernamePasswordToken(username,password);
				try {
					// 执行认证操作.
					subject.login(ut);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ResponseUtils.renderJson(response, json.toString());
	}
	
	
	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request, ModelMap model) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/operator/login.do";
	}
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; // 获取企业信息

}
