package com.jwzt.billing.action.ilive;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.billing.entity.EnterpriseAndProduct;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.entity.vo.EnterpriseProductInfo;
import com.jwzt.billing.manager.EnterpriseAndProductMng;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.billing.task.EnterpriseSyncTask;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class EnterpriseBillingAct {

	@RequestMapping(value = "/enterprise/info", method = RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap mp) {
		UserBO user = SessionUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		System.out.println("enterpriseId===================="+enterpriseId);
		Integer[] ids = { enterpriseId };
		EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask(ids);
		enterpriseSyncTask.run();
		EnterpriseBilling bean = enterpriseBillingMng.getBeanById(enterpriseId, true);
		RenderJsonUtils.addSuccess(mp, bean);
		return "renderJson";
	}

	@RequestMapping("/enterprise/productInfo")
	public String getProductInfo(HttpServletRequest request, ModelMap mp) {
		try {
			UserBO user = SessionUtils.getUser(request);
			Integer enterpriseId =user.getEnterpriseId();
			System.out.println("1:enterpriseId===================="+enterpriseId);
			EnterpriseProductInfo enterpriseProductInfo = enterpriseAndProductMng
					.getEnterpriseProductInfo(enterpriseId);
			System.out.println("PackageName=================="+enterpriseProductInfo.getPackageName());
			RenderJsonUtils.addSuccess(mp, enterpriseProductInfo);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}

	@RequestMapping("/enterprise/productList")
	public String getProductList(HttpServletRequest request, ModelMap mp) {
		try {
			UserBO user = SessionUtils.getUser(request);
			Integer enterpriseId = user.getEnterpriseId();
			System.out.println("2:enterpriseId===================="+enterpriseId);
			List<EnterpriseAndProduct> enterpriseAndProductList = enterpriseAndProductMng.listByEnterprise(enterpriseId,
					new Date());
			if (null != enterpriseAndProductList) {
				for (EnterpriseAndProduct enterpriseAndProduct : enterpriseAndProductList) {
					if (null != enterpriseAndProduct) {
						Integer productId = enterpriseAndProduct.getProductId();
						if (null != productId) {
							ProductInfo productInfo = productInfoMng.getBeanById(productId, true);
							if (null != productInfo) {
								enterpriseAndProduct.setProduct(productInfo);
							}
						}
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, enterpriseAndProductList);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}

	@Autowired
	private EnterpriseAndProductMng enterpriseAndProductMng;
	@Autowired
	private EnterpriseBillingMng enterpriseBillingMng;
	@Autowired
	private ProductInfoMng productInfoMng;
}
