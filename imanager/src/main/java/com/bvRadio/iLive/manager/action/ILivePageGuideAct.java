package com.bvRadio.iLive.manager.action;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * 页面导向控制类
 * 
 * @author administrator
 *
 */
@Controller
@RequestMapping(value = "page")
public class ILivePageGuideAct {

	@RequestMapping(value = "/{router}.do")
	public void pageGuide(@PathVariable(value = "router") String router, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 生存客户端登录token
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		// System.out.println("登录在线用户生存客户端登录token=" + token);
		UserBean user = ILiveUtils.getUser(request);
		CacheManager.putCacheInfo(CacheManager.third_token_ + token, user.getUserId(), 5 * 60 * 1000);
		String url = ConfigUtils.get(router);
		String redirectUrl ;
		if (url.indexOf("?") != -1) {
			redirectUrl = url + "&token=" + token;
		} else {
			redirectUrl = url + "?token=" + token;
		}
		response.sendRedirect(redirectUrl);
	}

}
