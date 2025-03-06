package com.bvRadio.iLive.iLive.action.front.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.AutoLogin;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

import net.sf.json.JSONObject;

@RequestMapping(value="cert")
@Controller
public class CertController {
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	private Logger logger = LoggerFactory.getLogger(CertController.class);
	
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
	@RequestMapping(value="cert.jspx")
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
							result.put("msg", "该账号不存在,请登录");
							response.sendRedirect(local_host+"/ilive/admin/cert/login.do");
						}else if (manager.getCertStatus()!=null&&manager.getCertStatus()!=4) {
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
