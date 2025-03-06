package com.jwzt.statistic.task;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.utils.ConfigUtils;

/**
 * 用户同步任务
 * 
 * @author ysf
 *
 */
public class UserSyncTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();
	private final List<Long> userIdList;

	public UserSyncTask(List<Long> userIdList) {
		super();
		this.userIdList = userIdList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			log.debug("UserSyncTask run.");
			UserInfoMng userInfoMng = (UserInfoMng) context.getBean("UserInfoMng");
			// 查询用户
			if (null != userIdList && userIdList.size() > 0) {
				try {
					StringBuilder paramsBuilder = new StringBuilder();
					for (Long userId : userIdList) {
						paramsBuilder.append("userIds[]").append("=").append(URLEncoder.encode(userId + "", "UTF-8"))
								.append("&");
					}
					String getUserInfoUrl = ConfigUtils.get(ConfigUtils.GET_USER_INFO_URL);
					String params = paramsBuilder.toString();
					String getUserInfoResultJson = HttpUtils.sendPost(getUserInfoUrl, params, "utf-8");
					if (StringUtils.isNotBlank(getUserInfoResultJson)) {
						Map<?, ?> getUserInfoResultMap = JsonUtils.jsonToMap(getUserInfoResultJson);
						Integer code = (Integer) getUserInfoResultMap.get("code");
						if (null != code && code == 1) {
							List<Map<?, ?>> dataList = (List<Map<?, ?>>) getUserInfoResultMap.get("data");
							if (null != dataList) {
								for (Map<?, ?> dataMap : dataList) {
									try {
										userInfoMng.saveOrUpdateFromDataMap(dataMap);
									} catch (Exception e) {
										log.warn("saveUserInfo error.", e);
									}
								}
							}

						}
					}
				} catch (Exception e) {
					log.warn("saveUserInfo error.", e);
				}
			}
			log.debug("UserSyncTask end.");
		} catch (Exception e) {
			log.error("UserSyncTask error.", e);
		}
	}
}