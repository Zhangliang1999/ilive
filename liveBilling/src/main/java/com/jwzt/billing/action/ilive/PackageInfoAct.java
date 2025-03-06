package com.jwzt.billing.action.ilive;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class PackageInfoAct {

	@RequestMapping(value = "/package/list", method = RequestMethod.GET)
	public String list(String queryContent,
			@RequestParam(value = "packageTypes", required = false) Integer[] packageTypes, Integer status,
			Integer channelType, Date startTime, Date endTime, ModelMap mp, HttpServletRequest request,
			Integer pageNo) {
		String channelTypes = null;
		String packageName = null;
		try {
			if (null != queryContent) {
				packageName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			if (null != channelType) {
				channelTypes = String.valueOf(channelType);
			}
		} catch (Exception e) {
		}
		List<PackageInfo> list = packageMng.listByParams(packageName, packageTypes, status, channelTypes, startTime,
				endTime, true, true);
		if (null != list) {
			for (PackageInfo bean : list) {
				if (null != bean) {
					Long durationProduct = bean.getDurationProduct();
					Long storageProduct = bean.getStorageProduct();
					if (null != durationProduct) {
						// 秒 转小时
						durationProduct = durationProduct / 3600;
						bean.setDurationProduct(durationProduct);
					}
					if (null != storageProduct) {
						// KB 转 G
						storageProduct = storageProduct / 1024 / 1024;
						bean.setStorageProduct(storageProduct);
					}
				}
			}
		}
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@RequestMapping(value = "/package/info", method = RequestMethod.GET)
	public String info(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		PackageInfo bean = packageMng.getBeanById(id, true);
		if (null != bean) {
			try {
				List<WorkLog> workLogList = workLogMng.listByParams(WorkLog.CURRENT_SYSTEM_ID, WorkLog.MODEL_ID_PACKAGE,
						String.valueOf(id), null, null, null);
				bean.setWorkLogList(workLogList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@Autowired
	private PackageInfoMng packageMng;
	@Autowired
	private WorkLogMng workLogMng;
}
