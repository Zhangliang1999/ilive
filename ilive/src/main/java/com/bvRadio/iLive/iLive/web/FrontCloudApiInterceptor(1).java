package com.bvRadio.iLive.iLive.web;

import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alipay.api.main;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.AppSecretUtil;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.jwzt.comm.StringUtils;

/**
 * 云直播底层服务 提供流媒体
 * 
 * @author administrator
 */
public class FrontCloudApiInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		/**
		 * 第一步：校验三个必要参数
		 */
		try {
			if (checkValidMustParams(request, response)) {
				/**
				 * 第二步：校验timestamp的时间合法性
				 */
				if (checkTimeValid(request, response)) {
					/**
					 * 第三步：校验加密串的合法性
					 */
					if (checkTokenValid(request, response)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}
		return false;
	}

	private boolean checkValidMustParams(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		if (StringUtils.isEmpty(userId)) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "userId必须包含在参数中");
			resultJson.put("data", "{}");
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}

		String timeStamp = request.getParameter("timeStamp");
		if (StringUtils.isEmpty(timeStamp)) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "timeStamp必须包含在参数中");
			resultJson.put("data", "{}");
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}

		String token = request.getParameter("token");
		if (StringUtils.isEmpty(token)) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "token必须包含在参数中");
			resultJson.put("data", "{}");
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}
		return true;
	}

	private boolean checkTimeValid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String timeStamp = request.getParameter("timeStamp");
		Long nowSeconds = System.currentTimeMillis() / 1000L;
		if (nowSeconds - Long.parseLong(timeStamp) > 300L) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "请求已过期");
			resultJson.put("data", "{}");
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}
		return true;

	}

	private boolean checkTokenValid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		String timeStamp = request.getParameter("timeStamp");
		String token = request.getParameter("token");
		String app_screct = AppSecretUtil.App_screct(userId);
		String encode = Md5Util.encode(String.format("%s%s%s", app_screct, userId, timeStamp));
		if (!token.equals(encode)) {
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "请求token不合法");
			resultJson.put("data", "{}");
			ResponseUtils.renderJson(response, resultJson.toString());
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String app_screct = AppSecretUtil.App_screct("100");
		System.out.println(app_screct);
		long t = System.currentTimeMillis() / 1000L;
		System.out.println(t);
		/**
		 * ef5cbdb7db57219c9f60cf8e08c6030a 
		 * 1528724202
		 * ff33ba9f049fb7a64e8317126aaba190
		 */
		String encode = Md5Util.encode(String.format("%s%s%s", app_screct, 100, t + ""));
		System.out.println(encode);

	}

}
