package com.jwzt.statistic.task;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.utils.ConfigUtils;

/**
 * 回看信息同步
 * @author gstars
 *
 */
public class VideoSyncTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	private static final Integer SYNC_MODE_LIST = 1;

	private static final Integer SYNC_MODE_SINGLE = 2;

	private static final Integer LIST_SIZE = 100;

	private Integer[] ids;

	private Integer syncMode;

	public VideoSyncTask() {
		super();
		syncMode = SYNC_MODE_LIST;
	}

	public VideoSyncTask(Integer[] ids) {
		super();
		this.ids = ids;
		syncMode = SYNC_MODE_SINGLE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			System.out.println("***************************************************************************start*");
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			log.debug("VideoSyncTask run. syncMode={}", syncMode);
			VideoInfoMng videoInfoMng = (VideoInfoMng) context.getBean("VideoInfoMng");
			System.out.println("************************syncMode:"+syncMode);
			if (SYNC_MODE_LIST.equals(syncMode)) {
				String getVideoListUrl = ConfigUtils.get(ConfigUtils.GET_VIDEO_LIST_URL);
				ConfigMng configMng = (ConfigMng) context.getBean("ConfigMng");
				String lastSyncVideoIdValue = configMng.getValue(Config.LAST_SYNC_VIDEO_ID);
				Long startId;
				try {
					startId = Long.parseLong(lastSyncVideoIdValue);
				} catch (Exception e) {
					startId = 0L;
				}
				Long newLastSyncVideoId = null;
				Integer resultSize = 0;
				do {
					resultSize = 0;
					StringBuilder paramsBuilder = new StringBuilder();
					paramsBuilder.append("startId").append("=").append(startId).append("&");
					paramsBuilder.append("size").append("=").append(LIST_SIZE).append("&");
					String params = paramsBuilder.toString();
					System.out.println(getVideoListUrl+"*******************************************");
					String getVideoListResultJson = HttpUtils.sendPost(getVideoListUrl, params, "utf-8");
					System.out.println("回看同步结果list   *******   "+getVideoListResultJson);
					Map<?, ?> getVideoListResultMap = JsonUtils.jsonToMap(getVideoListResultJson);
					// 回看同步
					Integer code = (Integer) getVideoListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getVideoListResultMap.get("data");
						if (null != dataList) {
							for (Map<?, ?> dataMap : dataList) {
								try {
									Object fileIdObj = dataMap.get("fileId");
									Long fileId;
									if (null != fileIdObj && Long.class.isInstance(fileIdObj)) {
										fileId = (Long) fileIdObj;
									} else {
										fileId = ((Integer) fileIdObj).longValue();
									}
									videoInfoMng.saveOrUpdateFromDataMap(dataMap);
									resultSize++;
									newLastSyncVideoId = fileId;
									startId = newLastSyncVideoId;
								} catch (Exception e) {
									log.warn("saveVideoInfo error.", e);
								}
							}
						}
					}
					if (null != newLastSyncVideoId) {
						configMng.updateOrSave(Config.LAST_SYNC_VIDEO_ID, newLastSyncVideoId + "");
					}
				} while (resultSize > 0);
			} else if (SYNC_MODE_SINGLE.equals(syncMode)) {
				// 回看根据ids获取信息
				String getVideoInfoUrl = ConfigUtils.get(ConfigUtils.GET_VIDEO_INFO_URL);
				if (null != ids && ids.length > 0) {
					StringBuilder paramsBuilder = new StringBuilder();
					for (Integer id : ids) {
						paramsBuilder.append("ids[]").append("=").append(id).append("&");
					}
					String params = paramsBuilder.toString();
					String getVideoListResultJson = HttpUtils.sendPost(getVideoInfoUrl, params, "utf-8");
					System.out.println("************************************getVideoListResultJson:"+getVideoListResultJson);
					Map<?, ?> getVideoListResultMap = JsonUtils.jsonToMap(getVideoListResultJson);
					// 回看同步
					Integer code = (Integer) getVideoListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getVideoListResultMap.get("data");
						if (null != dataList) {
							for (Map<?, ?> dataMap : dataList) {
								try {
									videoInfoMng.saveOrUpdateFromDataMap(dataMap);
								} catch (Exception e) {
									log.warn("saveVideoInfo error.", e);
								}
							}
						}
					}
				}
			}
			log.debug("VideoSyncTask end.");
			System.out.println("****************************************************************************执行完毕");
		} catch (Exception e) {
			log.error("VideoSyncTask error.", e);
			System.out.println("****************************************************************************异常");
		}
	}

}