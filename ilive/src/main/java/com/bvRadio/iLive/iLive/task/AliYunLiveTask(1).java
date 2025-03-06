package com.bvRadio.iLive.iLive.task;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.util.AliYunLiveUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class AliYunLiveTask extends TimerTask {

	public static final Integer MODEL_START = 0;
	public static final Integer MODEL_ADD = 1;
	public static final Integer MODEL_REMOVE = 2;
	private static final Logger log = LoggerFactory.getLogger(AliYunLiveTask.class);
	private Integer roomId;
	private Integer model;

	public AliYunLiveTask(Integer roomId, Integer model) {
		super();
		this.roomId = roomId;
		this.model = model;
	}

	public void run() {
		try {
			String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
			String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
			if (MODEL_START.equals(model)) {
				AliYunLiveUtils.startMultipleStreamMixService(aliyunLiveDomain, aliyunLiveAppname, roomId + "_host");
			} else if (MODEL_ADD.equals(model)) {
				AliYunLiveUtils.removeMultipleStreamMixService(aliyunLiveDomain, aliyunLiveAppname, roomId + "_host",
						roomId + "_user");
				Thread.sleep(1000);
				AliYunLiveUtils.addMultipleStreamMixService(aliyunLiveDomain, aliyunLiveAppname, roomId + "_host",
						roomId + "_user");
			} else if (MODEL_REMOVE.equals(model)) {
				AliYunLiveUtils.removeMultipleStreamMixService(aliyunLiveDomain, aliyunLiveAppname, roomId + "_host",
						roomId + "_user");
			}

		} catch (Exception e) {
			log.error("AliYunLiveTask error.", e);
		}
	}

}