package com.jwzt.billing.action.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.billing.action.admin.WelcomeAct;
import com.jwzt.billing.entity.EnterpriseAndProduct;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.entity.vo.EnterpriseProductInfo;
import com.jwzt.billing.manager.EnterpriseAndProductMng;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.billing.task.EnterpriseSyncTask;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

/**
 * 企业相关接口
 * 
 * @author ysf
 *
 */
@Controller
public class EnterpriseAct {

	/**
	 * 获得企业购买的产品信息
	 * @param enterpriseId
	 * @param certStatus
	 * @param mp
	 * @return
	 */
	@RequestMapping("/enterprise/getProductInfo")
	public String getProductInfo(final Integer enterpriseId, final Integer certStatus, ModelMap mp) {
		try {
			if (null == enterpriseId) {
				RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
				return "renderJson";
			}
			if (null == certStatus) {
				RenderJsonUtils.addNullParamsError(mp, "certStatus");
				return "renderJson";
			}
			List<EnterpriseAndProduct> currentEnterpriseAndProductList = enterpriseAndProductMng
					.listByEnterprise(enterpriseId, null);
			Integer defaultPackageId = null;
			if (null == currentEnterpriseAndProductList || currentEnterpriseAndProductList.size() == 0) {
				if (EnterpriseBilling.CERT_STATUS_SUCCESS.equals(certStatus)) {
					// 认证用户 试用 认证套餐
					defaultPackageId = Integer.parseInt(configMng.getValue(Config.CERT_USER_PACKAGE_ID));
				} else {
					// 试用用户 使用 试用套餐
					defaultPackageId = Integer.parseInt(configMng.getValue(Config.BETA_USER_PACKAGE_ID));
				}
			}else if(null != currentEnterpriseAndProductList || currentEnterpriseAndProductList.size() > 0){
				if(EnterpriseBilling.CERT_STATUS_SUCCESS.equals(certStatus)) {
					boolean isOnlyBatePackage = true; 
					for(EnterpriseAndProduct vaildEnterpriseAndProduct : currentEnterpriseAndProductList) {
						if(vaildEnterpriseAndProduct.getPackageId()!=Integer.parseInt(configMng.getValue(Config.BETA_USER_PACKAGE_ID))) {
							isOnlyBatePackage = false;
						}
						
					}
					if (isOnlyBatePackage) {//套餐id为使用套餐切认证状态为4
						// 认证用户 试用 认证套餐
						defaultPackageId = Integer.parseInt(configMng.getValue(Config.CERT_USER_PACKAGE_ID));
						try {
							String contentName = "试用套餐变更为认证套餐";
							WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_SPAYMENT, String.valueOf(enterpriseId), "", contentName, enterpriseId.longValue(),
									"", null);
							workLogMng.save(workLog);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			if (null != defaultPackageId) {
				enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, defaultPackageId, false, true,null);
			}
			EnterpriseProductInfo enterpriseProductInfo = enterpriseAndProductMng
					.getEnterpriseProductInfo(enterpriseId);
			
			//重新更新缓存
			WelcomeAct.recheckUsedCache(enterpriseId, certStatus);
			//查询计费系统企业表有没有该企业，没有的话执行一遍企业同步线程
			EnterpriseBilling bean = enterpriseBillingMng.getBeanById(enterpriseId, true);
			if(bean==null){
				EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask();
				enterpriseSyncTask.run();
			}
			RenderJsonUtils.addSuccess(mp, enterpriseProductInfo);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}

	/**
	 * 更新企业消耗的存储或者短信等
	 * @param enterpriseId
	 * @param certStatus
	 * @param useType
	 * @param useValue
	 * @param mp
	 * @return
	 */
	@RequestMapping("/enterprise/updateProductInfo")
	public String updateProductInfo(final Integer enterpriseId, final Integer certStatus, final Integer useType,
			final Long useValue, ModelMap mp) {
		try {
			if (null == enterpriseId) {
				RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
				return "renderJson";
			}
			if (null == useType) {
				RenderJsonUtils.addNullParamsError(mp, "useType");
				return "renderJson";
			}
			System.out.println("暂停时长消耗："+useValue);
			if (null == useValue) {
				RenderJsonUtils.addNullParamsError(mp, "useValue");
				return "renderJson";
			}
			if (null == certStatus) {
				RenderJsonUtils.addNullParamsError(mp, "certStatus");
				return "renderJson";
			}
			Long vaildValue = 0L;
			EnterpriseProductInfo enterpriseProductInfo = enterpriseAndProductMng
					.getEnterpriseProductInfo(enterpriseId);
			
			if (null != enterpriseProductInfo) {
				if (ProductInfo.PRODUCT_TYPE_CONCURRENCE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getConcurrenceProduct()
							-enterpriseProductInfo.getUsedConcurrenceProduct();
				} else if (ProductInfo.PRODUCT_TYPE_DURATION.equals(useType)) {
					vaildValue = enterpriseProductInfo.getDurationProduct()
							-enterpriseProductInfo.getUsedDurationProduct();
				} else if (ProductInfo.PRODUCT_TYPE_SHORT_MESSAGE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getShortMessageProduct()
							-enterpriseProductInfo.getUsedShortMessageProduct();
				} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getStorageProduct()
							-enterpriseProductInfo.getUsedStorageProduct();
				}
			}
			if (vaildValue < useValue&&!ProductInfo.PRODUCT_TYPE_DURATION.equals(useType)) {
				RenderJsonUtils.addError(mp, "余量不足");
				return "renderJson";
			}
			
			Long remainingUseValue = useValue;
			List<EnterpriseAndProduct> vaildEnterpriseAndProductList = enterpriseAndProductMng
					.listByEnterprise(enterpriseId, new Date());
			
			if (null != vaildEnterpriseAndProductList) {
				for (EnterpriseAndProduct vaildEnterpriseAndProduct : vaildEnterpriseAndProductList) {
					if (null != vaildEnterpriseAndProduct) {
						Integer productId = vaildEnterpriseAndProduct.getProductId();
						//根据productId去查找相应的
						Integer productType=productInfoMng.getBeanById(productId, false).getProductType();
						
						if (null != productId&&productType.equals(useType)) {
							ProductInfo productInfo = productInfoMng.getBeanById(productId, false);
							Long incrementValue = productInfo.getIncrementValue();
							if(incrementValue==null) {
								incrementValue=0L;
							}
							Long usedValue = vaildEnterpriseAndProduct.getUsedValue();
							
							if(usedValue==null) {
								usedValue=0L;
							}
							if (usedValue < incrementValue) {
								if (remainingUseValue <= (incrementValue - usedValue)) {
									
									vaildEnterpriseAndProduct
											.setUsedValue( usedValue + remainingUseValue);
									enterpriseAndProductMng.update(vaildEnterpriseAndProduct);
									break;
								} else {
									remainingUseValue = remainingUseValue - (incrementValue - usedValue);
									vaildEnterpriseAndProduct.setUsedValue(incrementValue);
									enterpriseAndProductMng.update(vaildEnterpriseAndProduct);
									continue;
								}
							} else {
								continue;
							}
						}
					}
				}
			}
			EnterpriseProductInfo enterpriseProductInfo1 = enterpriseAndProductMng
					.getEnterpriseProductInfo(enterpriseId);
            try {
				String contentName = "企业产品消耗";
				WorkLog workLog = new WorkLog(WorkLog.MODEL_ID_SPAYMENT, String.valueOf(enterpriseId), "", contentName, enterpriseId.longValue(),
						enterpriseId.toString(), null);
				workLogMng.save(workLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
          //重新更新缓存
			WelcomeAct.recheckUsedCache(enterpriseId, certStatus);
			RenderJsonUtils.addSuccess(mp, enterpriseProductInfo1);
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}
/**
 * 查询企业购买套餐类型余量
 */
	@RequestMapping("/enterprise/getProductRemin")
	public String getProductRemin(final Integer enterpriseId, final Integer certStatus, final Integer useType, ModelMap mp) {
		try {
			if (null == enterpriseId) {
				RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
				return "renderJson";
			}
			if (null == useType) {
				RenderJsonUtils.addNullParamsError(mp, "useType");
				return "renderJson";
			}
			if (null == certStatus) {
				RenderJsonUtils.addNullParamsError(mp, "certStatus");
				return "renderJson";
			}
			Long vaildValue = 0L;
			Long totalValue = 0L;
			EnterpriseProductInfo enterpriseProductInfo = enterpriseAndProductMng
					.getEnterpriseProductInfo(enterpriseId);
			if (null != enterpriseProductInfo) {
				if (ProductInfo.PRODUCT_TYPE_CONCURRENCE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getConcurrenceProduct()
					- enterpriseProductInfo.getUsedConcurrenceProduct();
					totalValue=enterpriseProductInfo.getConcurrenceProduct();
				} else if (ProductInfo.PRODUCT_TYPE_DURATION.equals(useType)) {
					vaildValue = enterpriseProductInfo.getDurationProduct()
					-enterpriseProductInfo.getUsedDurationProduct();
					totalValue=enterpriseProductInfo.getDurationProduct();
				} else if (ProductInfo.PRODUCT_TYPE_SHORT_MESSAGE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getShortMessageProduct()
					-enterpriseProductInfo.getUsedShortMessageProduct();
					totalValue=enterpriseProductInfo.getShortMessageProduct();
				} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(useType)) {
					vaildValue = enterpriseProductInfo.getStorageProduct()
					-enterpriseProductInfo.getUsedStorageProduct();
					totalValue=enterpriseProductInfo.getStorageProduct();
				}
			}
			System.out.println("vaildValue=========="+vaildValue);
			if (vaildValue<= 0L&&totalValue>0L) {
				RenderJsonUtils.addError(mp, "0");
				return "renderJson";
			}else {
				RenderJsonUtils.addSuccess(mp, "1");
				return "renderJson";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}
	/**
	 * 查询企业购买存储类型余量
	 */
		@RequestMapping("/enterprise/getProductStoreRemin")
		public String getProductStoreRemin(final Integer enterpriseId, final Integer certStatus, final Integer useType,final Long useValue, ModelMap mp) {
			try {
				if (null == enterpriseId) {
					RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
					return "renderJson";
				}
				if (null == useType) {
					RenderJsonUtils.addNullParamsError(mp, "useType");
					return "renderJson";
				}
				if (null == certStatus) {
					RenderJsonUtils.addNullParamsError(mp, "certStatus");
					return "renderJson";
				}
				Long vaildValue = 0L;
				Long totalValue = 0L;
				EnterpriseProductInfo enterpriseProductInfo = enterpriseAndProductMng
						.getEnterpriseProductInfo(enterpriseId);
				if (null != enterpriseProductInfo) {
					if (ProductInfo.PRODUCT_TYPE_CONCURRENCE.equals(useType)) {
						vaildValue = enterpriseProductInfo.getConcurrenceProduct()
						- enterpriseProductInfo.getUsedConcurrenceProduct();
						totalValue=enterpriseProductInfo.getConcurrenceProduct();
					} else if (ProductInfo.PRODUCT_TYPE_DURATION.equals(useType)) {
						vaildValue = enterpriseProductInfo.getDurationProduct()
						-enterpriseProductInfo.getUsedDurationProduct();
						totalValue=enterpriseProductInfo.getDurationProduct();
					} else if (ProductInfo.PRODUCT_TYPE_SHORT_MESSAGE.equals(useType)) {
						vaildValue = enterpriseProductInfo.getShortMessageProduct()
						-enterpriseProductInfo.getUsedShortMessageProduct();
						totalValue=enterpriseProductInfo.getShortMessageProduct();
					} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(useType)) {
						vaildValue = enterpriseProductInfo.getStorageProduct()
						-enterpriseProductInfo.getUsedStorageProduct();
						totalValue=enterpriseProductInfo.getStorageProduct();
					}
				}
				System.out.println("vaildValue=========="+vaildValue);
				if (vaildValue<= useValue&&totalValue>=0L) {
					RenderJsonUtils.addError(mp, "0");
					return "renderJson";
				}else {
					RenderJsonUtils.addSuccess(mp, "1");
					return "renderJson";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				RenderJsonUtils.addError(mp, "服务异常");
			}
			return "renderJson";
		}
	/**
	 * 查询企业是否开通收益账户
	 * @param enterpriseId
	 * @param mp
	 * @return
	 */
	@RequestMapping("/enterprise/getEnterpriseRevenue")
	public String getEnterpriseRevenue(final Integer enterpriseId, ModelMap mp) {
		try {
			if (null == enterpriseId) {
				RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
				return "renderJson";
			}
			EnterpriseBilling bean = enterpriseBillingMng.getBeanById(enterpriseId, true);
			if(bean==null) {
				RenderJsonUtils.addError(mp, "0");
				return "renderJson";
			}else {
			Long revenueAccountWorkOrderId=bean.getRevenueAccountWorkOrderId();
			if(revenueAccountWorkOrderId==null) {
				revenueAccountWorkOrderId=0L;
			}
			if(revenueAccountWorkOrderId>0L) {
				RenderJsonUtils.addSuccess(mp, "1");
				return "renderJson";
			}else {
				RenderJsonUtils.addError(mp, "0");
				return "renderJson";
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}
	/**
	 * 查询企业是否开通收益账户
	 * @param enterpriseId
	 * @param mp
	 * @return
	 */
	@RequestMapping("/enterprise/totalUpdateProduct")
	public String totalUpdateProduct(final Integer enterpriseId,final String useType,final Integer certStatus,final String total, ModelMap mp) {
		try {
			if (null == enterpriseId) {
				RenderJsonUtils.addNullParamsError(mp, "enterpriseId");
				return "renderJson";
			}
			if (null == useType) {
				RenderJsonUtils.addNullParamsError(mp, "useType");
				return "renderJson";
			}
			if (null == certStatus) {
				RenderJsonUtils.addNullParamsError(mp, "certStatus");
				return "renderJson";
			}
			if (null == total) {
				RenderJsonUtils.addNullParamsError(mp, "total");
				return "renderJson";
			}
			Long remainingUseValue = Long.parseLong(total);
			List<EnterpriseAndProduct> vaildEnterpriseAndProductList = enterpriseAndProductMng
					.listByEnterprise(enterpriseId, new Date());
			
			if (null != vaildEnterpriseAndProductList) {
				for (EnterpriseAndProduct vaildEnterpriseAndProduct : vaildEnterpriseAndProductList) {
					if (null != vaildEnterpriseAndProduct) {
						Integer productId = vaildEnterpriseAndProduct.getProductId();
						//根据productId去查找相应的
						Integer productType=productInfoMng.getBeanById(productId, false).getProductType();
						
						if (null != productId&&productType.equals(Integer.parseInt(useType))) {
							ProductInfo productInfo = productInfoMng.getBeanById(productId, false);
							Long incrementValue = productInfo.getIncrementValue();
							if(incrementValue==null) {
								incrementValue=0L;
							}
							Long usedValue = 0L;
							
							if (usedValue < incrementValue) {
								if (remainingUseValue <= (incrementValue - usedValue)) {
									
									vaildEnterpriseAndProduct
											.setUsedValue( usedValue + remainingUseValue);
									enterpriseAndProductMng.update(vaildEnterpriseAndProduct);
									break;
								} else {
									remainingUseValue = remainingUseValue - (incrementValue - usedValue);
									vaildEnterpriseAndProduct.setUsedValue(incrementValue);
									enterpriseAndProductMng.update(vaildEnterpriseAndProduct);
									continue;
								}
							} else {
								continue;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp, "服务异常");
		}
		return "renderJson";
	}
	@Autowired
	private EnterpriseAndProductMng enterpriseAndProductMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private ProductInfoMng productInfoMng;
	@Autowired
	private EnterpriseBillingMng enterpriseBillingMng;
	@Autowired
	private WorkLogMng workLogMng;
}
