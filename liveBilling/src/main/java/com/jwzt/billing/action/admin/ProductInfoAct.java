package com.jwzt.billing.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

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

import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class ProductInfoAct {
	@RequestMapping(value = "/product/page", method = RequestMethod.GET)
	public String page(String queryContent,
			@RequestParam(value = "productTypes", required = false) Integer[] productTypes, Date startTime,
			Date endTime, ModelMap mp, HttpServletRequest request, Integer pageNo) {
		String productName = null;
		try {
			if (null != queryContent) {
				productName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
		} catch (Exception e) {
		}
		Pagination page = productMng.pageByParams(productName, productTypes, startTime, endTime, checkPageNo(pageNo),
				CookieUtils.getPageSize(request), true);
		RenderJsonUtils.addSuccess(mp, page);
		return "renderJson";
	}

	@RequestMapping(value = "/product/list", method = RequestMethod.GET)
	public String list(String queryContent,
			@RequestParam(value = "productTypes", required = false) Integer[] productTypes, Date startTime,
			Date endTime, ModelMap mp) {
		String productName = null;
		try {
			if (null != queryContent) {
				productName = new String(queryContent.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
		} catch (Exception e) {
		}
		List<ProductInfo> list = productMng.listByParams(productName, productTypes, startTime, endTime, true);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@RequestMapping(value = "/product/info", method = RequestMethod.GET)
	public String info(Integer id, ModelMap mp) {
		if (null == id) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		ProductInfo bean = productMng.getBeanById(id, true);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/product/save", method = RequestMethod.POST)
	public String save(ProductInfo bean, ModelMap mp) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		Integer productType = bean.getProductType();
		Long incrementValue = bean.getIncrementValue();
		if (null != incrementValue) {
			if (ProductInfo.PRODUCT_TYPE_DURATION.equals(productType)) {
				// 小时 转 秒
				incrementValue = incrementValue * 3600;
			} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(productType)) {
				// G 转 KB
				incrementValue = incrementValue * 1024 * 1024;
			}
			bean.setIncrementValue(incrementValue);
		}
		productMng.save(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public String update(ProductInfo bean, ModelMap mp) {
		if (null == bean) {
			RenderJsonUtils.addError(mp, "参数id不能为空");
			return "renderJson";
		}
		Integer productType = bean.getProductType();
		Long incrementValue = bean.getIncrementValue();
		if (null != incrementValue) {
			if (ProductInfo.PRODUCT_TYPE_DURATION.equals(productType)) {
				// 小时 转 秒
				incrementValue = incrementValue * 3600;
			} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(productType)) {
				// G 转 KB
				incrementValue = incrementValue * 1024 * 1024;
			}
			bean.setIncrementValue(incrementValue);
		}
		productMng.update(bean);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@Autowired
	private ProductInfoMng productMng;
}
