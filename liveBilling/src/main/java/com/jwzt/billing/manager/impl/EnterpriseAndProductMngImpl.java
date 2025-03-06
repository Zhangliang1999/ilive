package com.jwzt.billing.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.EnterpriseAndProductDao;
import com.jwzt.billing.entity.EnterpriseAndProduct;
import com.jwzt.billing.entity.PackageAndProduct;
import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.entity.vo.EnterpriseProductInfo;
import com.jwzt.billing.manager.EnterpriseAndProductMng;
import com.jwzt.billing.manager.PackageAndProductMng;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;

/**
 * @author ysf
 */
@Service
public class EnterpriseAndProductMngImpl implements EnterpriseAndProductMng {

	@Override
	@Transactional(readOnly = true)
	public EnterpriseProductInfo getEnterpriseProductInfo(Integer enterpriseId) {
		EnterpriseProductInfo enterpriseProductInfo = null;
		if (null != enterpriseId) {
			String functionCode = null;
			Long concurrenceProduct = 0L;
			Long durationProduct = 0L;
			Long shortMessageProduct = 0L;
			Long storageProduct = 0L;
			Long usedConcurrenceProduct = 0L;
			Long usedDurationProduct = 0L;
			Long usedShortMessageProduct = 0L;
			Long usedStorageProduct = 0L;
			Long subCountProduct=0L;
			Long usedSubCountProduct=0L;
			String packageName = null;
			Integer packageId =null;
			Timestamp vaildStartTime = null;
			Timestamp vaildEndTime = null;
			List<EnterpriseAndProduct> enterpriseAndProductList = listByEnterprise(enterpriseId, new Date());
			if (null != enterpriseAndProductList) {
				boolean ret=true;
				for(EnterpriseAndProduct ifyongjiu:enterpriseAndProductList){
					Integer id=ifyongjiu.getProductId();
					if(id!=null){
						ProductInfo product = productMng.getBeanById(id, false);
						if(product!=null){
							Integer productType = product.getProductType();
							 if (ProductInfo.PRODUCT_TYPE_DURATION.equals(productType)){
								 Long incrementValue = product.getIncrementValue();
								 if(incrementValue!=null&&incrementValue==-3600L){
									 ret=false;
									 break;
								 }
							 }
						}
					}
				}
				Set<String> set = new HashSet<String>();
				for (EnterpriseAndProduct enterpriseAndProduct : enterpriseAndProductList) {
					if (null != enterpriseAndProduct) {
						Integer productId = enterpriseAndProduct.getProductId();
						if (null != productId) {
							ProductInfo product = productMng.getBeanById(productId, false);
							if (null != product) {
								Integer productType = product.getProductType();
								if (ProductInfo.PRODUCT_TYPE_CONCURRENCE.equals(productType)) {
									Long incrementValue = product.getIncrementValue();
									if (null != incrementValue) {
										concurrenceProduct += incrementValue;
									}
									Long usedValue = enterpriseAndProduct.getUsedValue();
									if (null != usedValue) {
										usedConcurrenceProduct += usedValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_DURATION.equals(productType)) {
									Long incrementValue = product.getIncrementValue();
									if (null != incrementValue) {
										if(!ret){
											durationProduct=-3600L;
										}else{
											durationProduct += incrementValue;
										}
									}
									Long usedValue = enterpriseAndProduct.getUsedValue();
									if (null != usedValue) {
										usedDurationProduct += usedValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_SHORT_MESSAGE.equals(productType)) {
									Long incrementValue = product.getIncrementValue();
									if (null != incrementValue) {
										shortMessageProduct += incrementValue;
									}
									Long usedValue = enterpriseAndProduct.getUsedValue();
									if (null != usedValue) {
										usedShortMessageProduct += usedValue;
									}
								}else if (ProductInfo.PRODUCT_TYPE_SUBCOUNT.equals(productType)) {
									Long incrementValue = product.getIncrementValue();
									if (null != incrementValue) {
										subCountProduct += incrementValue;
									}
									Long usedValue = enterpriseAndProduct.getUsedValue();
									if (null != usedValue) {
										usedSubCountProduct += usedValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(productType)) {
									Long incrementValue = product.getIncrementValue();
									if (null != incrementValue) {
										storageProduct += incrementValue;
									}
									Long usedValue = enterpriseAndProduct.getUsedValue();
									if (null != usedValue) {
										usedStorageProduct += usedValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_BASIC.equals(productType)) {
									String functionCodes = product.getFunctionCodes();
									String[] functionCodeArray = functionCodes.split(",");
									// 遍历数组并存入集合,如果元素已存在则不会重复存入
									for (int i = 0; i < functionCodeArray.length; i++) {
										set.add(functionCodeArray[i]);
									}
									packageId = enterpriseAndProduct.getPackageId();
									vaildStartTime = enterpriseAndProduct.getVaildStartTime();
									vaildEndTime = enterpriseAndProduct.getVaildEndTime();
									if (null != packageId) {
										PackageInfo packageInfo = packageInfoMng.getBeanById(packageId, false);
										if (null != packageInfo) {
											packageName = packageInfo.getPackageName();
										}
									}
								}
							}
						}

					}
				}
				functionCode = StringUtils.join(set.toArray(), ",");
			}
			enterpriseProductInfo = new EnterpriseProductInfo(functionCode, concurrenceProduct, durationProduct,
					shortMessageProduct, storageProduct, usedConcurrenceProduct, usedDurationProduct,
					usedShortMessageProduct, usedStorageProduct,subCountProduct,usedSubCountProduct);
			enterpriseProductInfo.setPackageId(packageId);
			enterpriseProductInfo.setPackageName(packageName);
			enterpriseProductInfo.setVaildStartTime(vaildStartTime);
			enterpriseProductInfo.setVaildEndTime(vaildEndTime);
		}
		return enterpriseProductInfo;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnterpriseAndProduct> listByEnterprise(Integer enterpriseId, Date vaildCheckTime) {
		return dao.listByParams(enterpriseId, null, vaildCheckTime);
	}

	@Override
	@Transactional
	public EnterpriseAndProduct save(EnterpriseAndProduct bean) {
		if (null != bean) {
			String id = UUID.randomUUID().toString();
			bean.setId(id);
			bean.initFieldValue();
			dao.save(bean);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseAndProduct update(EnterpriseAndProduct bean) {
		if (null != bean) {
			dao.update(bean);
		}
		return bean;
	}

	@Override
	@Transactional
	public void insertOrUpgradePackage(final Integer enterpriseId, final Integer packageId,
			final boolean continueInsert, final boolean ignoreStatus,Timestamp startTime) {
		final PackageInfo packageInfo = packageInfoMng.getBeanById(packageId, false);
		if (null != packageInfo) {
			Integer status = packageInfo.getStatus();
			Integer packageType = packageInfo.getPackageType();
			if (ignoreStatus || PackageInfo.STATUS_ONLINE.equals(status)) {
				if (PackageInfo.PACKAGE_TYPE_BASIC.equals(packageType)) {
					// 基础套餐
					List<EnterpriseAndProduct> currentEnterpriseAndProductList = listByEnterprise(enterpriseId,
							new Date());
					EnterpriseAndProduct currentBasicEnterpriseAndProduct = getCurrentBasicEnterpriseAndProduct(
							currentEnterpriseAndProductList);
					if (null == currentBasicEnterpriseAndProduct) {
						// 当前没有有效的基础产品，直接添加
						insertBasicPackage(packageInfo, enterpriseId,startTime);
					} else if (continueInsert) {
						// 当前有有效的基础产品，是续购，直接添加
						continueInsertBasicPackage(packageInfo, enterpriseId, currentBasicEnterpriseAndProduct);
					} else if(ignoreStatus){
						insertNewPackage(packageInfo, enterpriseId, currentBasicEnterpriseAndProduct,
								currentEnterpriseAndProductList);
					}else {
						// 当前有有效的基础产品，不是续购，升级
						upgradeBasicPackage(packageInfo, enterpriseId, currentBasicEnterpriseAndProduct,
								currentEnterpriseAndProductList);
					}
				} else {
					// 增量套餐，直接添加
					insertBasicPackage(packageInfo, enterpriseId,startTime);
				}
			}
		}
	}

	private void insertNewPackage(PackageInfo packageInfo, Integer enterpriseId,
			EnterpriseAndProduct currentBasicEnterpriseAndProduct,
			List<EnterpriseAndProduct> currentEnterpriseAndProductList) {
	 if (null != packageInfo && null != currentBasicEnterpriseAndProduct) {
		Integer currentPackageId = currentBasicEnterpriseAndProduct.getPackageId();
		if (null != currentPackageId) {
			Integer vaildDurationUnit = packageInfo.getVaildDurationUnit();
				// 原套餐是试用或认证套餐，可以升级
				Integer packageType = packageInfo.getPackageType();
				if (PackageInfo.PACKAGE_TYPE_BASIC.equals(packageType)) {
					// 套餐类型是基础套餐才可升级
					// 计算订购套餐的有效时间
					long currentTimeMillis = System.currentTimeMillis();
					Timestamp vaildStartTime = new Timestamp(currentTimeMillis);
					Timestamp vaildEndTime = new Timestamp(currentTimeMillis);
					Integer vaildDurationValue = packageInfo.getVaildDurationValue();
					if (null != vaildDurationUnit && null != vaildDurationValue) {
						if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
							vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusDays(vaildDurationValue).getMillis());
						} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
							vaildEndTime = new Timestamp(
									new DateTime(vaildStartTime).plusMonths(vaildDurationValue).getMillis());
						} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
							vaildEndTime = new Timestamp(
									new DateTime(vaildStartTime).plusYears(vaildDurationValue).getMillis());
						}
					}
					Integer packageId = packageInfo.getId();
					saveEnterpriseAndPackage(packageId, enterpriseId, vaildStartTime, vaildEndTime);
					// 更新现有套餐的有效结束时间
					updateVaildEndTime(currentPackageId, currentEnterpriseAndProductList);
					
				}
			
		}
	}
		
	}

	private EnterpriseAndProduct getCurrentBasicEnterpriseAndProduct(
			List<EnterpriseAndProduct> currentEnterpriseAndProductList) {
		EnterpriseAndProduct currentBasicEnterpriseAndProduct = null;
		if (null != currentEnterpriseAndProductList) {
			for (EnterpriseAndProduct enterpriseAndProduct : currentEnterpriseAndProductList) {
				if (null != enterpriseAndProduct) {
					Integer productId = enterpriseAndProduct.getProductId();
					if (null != productId) {
						ProductInfo productInfo = productInfoMng.getBeanById(productId, false);
						if (null != productInfo) {
							Integer productType = productInfo.getProductType();
							if (ProductInfo.PRODUCT_TYPE_BASIC.equals(productType)) {
								currentBasicEnterpriseAndProduct = enterpriseAndProduct;
							}
						}
					}
				}
			}
		}
		return currentBasicEnterpriseAndProduct;
	}

	private void insertBasicPackage(final PackageInfo packageInfo, final Integer enterpriseId,Timestamp startTime) {
		if (null != packageInfo) {
			// 计算订购套餐的有效时间
			Timestamp vaildStartTime = startTime;
			Timestamp vaildEndTime = null;
			Integer vaildDurationUnit = packageInfo.getVaildDurationUnit();
			Integer vaildDurationValue = packageInfo.getVaildDurationValue();
			if(vaildStartTime==null){
				vaildStartTime = new Timestamp(System.currentTimeMillis());
			}
			vaildEndTime = null;
			if (null != vaildDurationUnit && null != vaildDurationValue) {
				if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusDays(vaildDurationValue).getMillis());
				} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusMonths(vaildDurationValue).getMillis());
				} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusYears(vaildDurationValue).getMillis());
				}
			}
			if (null == vaildEndTime) {
				vaildEndTime = vaildStartTime;
			}
			Integer packageId = packageInfo.getId();
			saveEnterpriseAndPackage(packageId, enterpriseId, vaildStartTime, vaildEndTime);
		}
	}

	private void continueInsertBasicPackage(final PackageInfo packageInfo, final Integer enterpriseId,
			final EnterpriseAndProduct currentBasicEnterpriseAndProduct) {
		if (null != packageInfo) {
			// 计算订购套餐的有效时间
			Timestamp vaildStartTime = null;
			Timestamp vaildEndTime = null;
			vaildStartTime = currentBasicEnterpriseAndProduct.getVaildEndTime();
			vaildEndTime = null;
			Integer vaildDurationUnit = packageInfo.getVaildDurationUnit();
			Integer vaildDurationValue = packageInfo.getVaildDurationValue();
			if (null != vaildDurationUnit && null != vaildDurationValue) {
				if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusDays(vaildDurationValue).getMillis());
				} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(
							new DateTime(vaildStartTime).plusMonths(vaildDurationValue).getMillis());
				} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
					vaildEndTime = new Timestamp(
							new DateTime(vaildStartTime).plusYears(vaildDurationValue).getMillis());
				}
			}
			if (null == vaildEndTime) {
				vaildEndTime = vaildStartTime;
			}
			Integer packageId = packageInfo.getId();
			saveEnterpriseAndPackage(packageId, enterpriseId, vaildStartTime, vaildEndTime);
		}
	}

	private void upgradeBasicPackage(final PackageInfo packageInfo, final Integer enterpriseId,
			final EnterpriseAndProduct currentBasicEnterpriseAndProduct,
			final List<EnterpriseAndProduct> currentEnterpriseAndProductList) {
		if (null != packageInfo && null != currentBasicEnterpriseAndProduct) {
			Integer currentPackageId = currentBasicEnterpriseAndProduct.getPackageId();
			if (null != currentPackageId) {
				PackageInfo currentPackage = packageInfoMng.getBeanById(currentPackageId, false);
				Integer vaildDurationUnit = packageInfo.getVaildDurationUnit();
				Integer currentVaildDurationUnit = currentPackage.getVaildDurationUnit();
				String betaPcakageId = configMng.getValue(Config.BETA_USER_PACKAGE_ID);
				String certPcakageId = configMng.getValue(Config.CERT_USER_PACKAGE_ID);
				  if (String.valueOf(currentPackageId).equals(certPcakageId)
						|| String.valueOf(currentPackageId).equals(betaPcakageId)) {
					// 原套餐是试用或认证套餐，可以升级
					Integer packageType = packageInfo.getPackageType();
					if (PackageInfo.PACKAGE_TYPE_BASIC.equals(packageType)) {
						// 套餐类型是基础套餐才可升级
						// 计算订购套餐的有效时间
						
						long currentTimeMillis = System.currentTimeMillis();
						Timestamp vaildStartTime = new Timestamp(currentTimeMillis);
						Timestamp vaildEndTime = new Timestamp(currentTimeMillis);
						Integer vaildDurationValue = packageInfo.getVaildDurationValue();
						if (null != vaildDurationUnit && null != vaildDurationValue) {
							if (PackageInfo.VAILD_DURATION_UNIT_DAY.equals(vaildDurationUnit)) {
								vaildEndTime = new Timestamp(new DateTime(vaildStartTime).plusDays(vaildDurationValue).getMillis());
							} else if (PackageInfo.VAILD_DURATION_UNIT_MONTH.equals(vaildDurationUnit)) {
								vaildEndTime = new Timestamp(
										new DateTime(vaildStartTime).plusMonths(vaildDurationValue).getMillis());
							} else if (PackageInfo.VAILD_DURATION_UNIT_YEAR.equals(vaildDurationUnit)) {
								vaildEndTime = new Timestamp(
										new DateTime(vaildStartTime).plusYears(vaildDurationValue).getMillis());
							}
						}
						Integer packageId = packageInfo.getId();
						saveEnterpriseAndPackage(packageId, enterpriseId, vaildStartTime, vaildEndTime);
						// 更新现有套餐的有效结束时间
						updateVaildEndTime(currentPackageId, currentEnterpriseAndProductList);
						
					}
				}else if (vaildDurationUnit.equals(currentVaildDurationUnit)) {
					// 有有效套餐，且要升级的套餐的套餐周期和现有的一致，可以升级
					Integer packageType = packageInfo.getPackageType();
					if (PackageInfo.PACKAGE_TYPE_BASIC.equals(packageType)) {
						// 套餐类型是基础套餐才可升级
						// 计算订购套餐的有效时间
						long currentTimeMillis = System.currentTimeMillis();
						Timestamp vaildStartTime = new Timestamp(currentTimeMillis);
						Timestamp vaildEndTime = currentBasicEnterpriseAndProduct.getVaildEndTime();
						Integer packageId = packageInfo.getId();
						saveEnterpriseAndPackage(packageId, enterpriseId, vaildStartTime, vaildEndTime);
						// 更新现有套餐的有效结束时间
						updateVaildEndTime(currentPackageId, currentEnterpriseAndProductList);
					}
				}
			}
		}
	}

	private void saveEnterpriseAndPackage(final Integer packageId, final Integer enterpriseId,
			final Timestamp vaildStartTime, final Timestamp vaildEndTime) {
		if (null == packageId || null == enterpriseId || null == vaildStartTime || null == vaildEndTime) {
			return;
		}
		List<PackageAndProduct> packageAndProductList = packageAndProductMng.listByPackage(packageId);
		if (null != packageAndProductList) {
			for (PackageAndProduct packageAndProduct : packageAndProductList) {
				if (null != packageAndProduct) {
					Integer productId = packageAndProduct.getProductId();
					if (null != productId) {
						ProductInfo productInfo = productInfoMng.getBeanById(productId, false);
						if (null != productInfo) {
							EnterpriseAndProduct enterpriseAndProduct = new EnterpriseAndProduct();
							enterpriseAndProduct.setEnterpriseId(enterpriseId);
							enterpriseAndProduct.setProductId(productId);
							enterpriseAndProduct.setVaildStartTime(vaildStartTime);
							enterpriseAndProduct.setVaildEndTime(vaildEndTime);
							enterpriseAndProduct.setPackageId(packageId);
							save(enterpriseAndProduct);
						}
					}
				}
			}
		}
	}

	private void updateVaildEndTime(final Integer packageId,
			final List<EnterpriseAndProduct> currentEnterpriseAndProductList) {
		// 更新现有套餐的有效时间
		if (null != currentEnterpriseAndProductList) {
			for (EnterpriseAndProduct currentEnterpriseAndProduct : currentEnterpriseAndProductList) {
				if (null != currentEnterpriseAndProduct) {
					Integer currentPackageId = currentEnterpriseAndProduct.getPackageId();
					if (currentPackageId.equals(packageId)) {
						currentEnterpriseAndProduct.setVaildEndTime(new Timestamp(System.currentTimeMillis()));
						update(currentEnterpriseAndProduct);
					}
				}
			}
		}
	}

	@Autowired
	private EnterpriseAndProductDao dao;
	@Autowired
	private ProductInfoMng productMng;
	@Autowired
	private PackageInfoMng packageInfoMng;
	@Autowired
	private ProductInfoMng productInfoMng;
	@Autowired
	private PackageAndProductMng packageAndProductMng;
	@Autowired
	private ConfigMng configMng;
}
