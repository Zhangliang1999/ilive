package com.jwzt.billing.task;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.common.utils.HttpUtils;

public class IliveNotifyTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	public static final Integer NOTIFY_MODE_UPDATE_PRODUCT = 1;

	private Integer[] ids;
	private Integer notifyMode;

	public IliveNotifyTask(Integer[] ids, Integer notifyMode) {
		super();
		this.ids = ids;
		this.notifyMode = notifyMode;
	}

	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				context = ContextLoader.getCurrentWebApplicationContext();
			}
			log.debug("IliveNotifyTask run. notifyMode={}", notifyMode);
			if (NOTIFY_MODE_UPDATE_PRODUCT.equals(notifyMode)) {
				String updateProductUrl = ConfigUtils.get("ilive_enterprise_update_product_url");
				if (null != ids) {
					for (Integer id : ids) {
						if (null != id) {
							Map<String, String> params = new HashMap<String, String>();
							params.put("enterpriseId", String.valueOf(id));
							HttpUtils.sendPost(updateProductUrl, params, "utf-8");
						}
					}
				}
			}
			log.debug("IliveNotifyTask end.");
		} catch (Exception e) {
			log.error("IliveNotifyTask error.", e);
		}
	}

}