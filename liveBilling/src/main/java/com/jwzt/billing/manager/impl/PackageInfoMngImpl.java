package com.jwzt.billing.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.PackageInfoDao;
import com.jwzt.billing.entity.PackageAndProduct;
import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.manager.PackageAndProductMng;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.common.entity.Config;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class PackageInfoMngImpl implements PackageInfoMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean initBean, boolean orderByNum) {
		Pagination page = dao.pageByParams(packageName, packageTypes, status, channelTypes, startTime, endTime, pageNo,
				pageSize, orderByNum);
		if (null != page && initBean) {
			List<PackageInfo> list = (List<PackageInfo>) page.getList();
			if (null != list) {
				for (PackageInfo bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PackageInfo> listByParams(String packageName, Integer[] packageTypes, Integer status,
			String channelTypes, Date startTime, Date endTime, boolean initBean, boolean orderByNum) {
		List<PackageInfo> list = dao.listByParams(packageName, packageTypes, status, channelTypes, startTime, endTime,
				orderByNum);
		if (null != list && initBean) {
			for (PackageInfo bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public PackageInfo getBeanById(Integer id, boolean initBean) {
		PackageInfo bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PackageInfo save(final PackageInfo bean, final Integer[] productIds) {
		if (null != bean) {
			Long nextId = fieldIdManagerMng.getNextId("billing_package", "id", 1L);
			if (null != nextId && nextId > 0) {
				int packageId = nextId.intValue();
				bean.setId(nextId.intValue());
				bean.initFieldValue();
				dao.save(bean);
				if (null != productIds) {
					for (Integer productId : productIds) {
						if (null != productId) {
							ProductInfo productInfo = productInfoMng.getBeanById(productId, false);
							if (null != productInfo) {
								packageAndProductMng.save(packageId, productId);
							}
						}
					}
				}
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PackageInfo update(PackageInfo bean) {
		if (null != bean) {
			Updater<PackageInfo> updater = new Updater<PackageInfo>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	private void initBean(final PackageInfo bean) {
		if (null != bean) {
			String introduceImagePath = bean.getIntroduceImagePath();
			if (StringUtils.isNotBlank(introduceImagePath)) {
				String httpUrl = configMng.getValue(Config.OSS_SERVER_HOME_URL);
				if (StringUtils.isNotBlank(httpUrl)) {
					if (!httpUrl.endsWith("/") && !introduceImagePath.startsWith("/")) {
						bean.setIntroduceImagePath(httpUrl + "/" + introduceImagePath);
					} else {
						bean.setIntroduceImagePath(httpUrl + introduceImagePath);
					}
				}
			}
			Integer packageId = bean.getId();
			if (null != packageId) {
				List<PackageAndProduct> packageAndProductList = packageAndProductMng.listByPackage(packageId);
				if (null != packageAndProductList) {
					List<ProductInfo> productList = new ArrayList<ProductInfo>();
					Long concurrenceProduct = 0L;
					Long durationProduct = 0L;
					Long shortMessageProduct = 0L;
					Long storageProduct = 0L;
					for (PackageAndProduct packageAndProduct : packageAndProductList) {
						if (null != packageAndProduct) {
							Integer productId = packageAndProduct.getProductId();
							ProductInfo productInfo = productInfoMng.getBeanById(productId, true);
							if (null != productInfo) {
								productList.add(productInfo);
								Integer productType = productInfo.getProductType();
								if (ProductInfo.PRODUCT_TYPE_CONCURRENCE.equals(productType)) {
									Long incrementValue = productInfo.getIncrementValue();
									if (null != incrementValue) {
										concurrenceProduct += incrementValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_DURATION.equals(productType)) {
									Long incrementValue = productInfo.getIncrementValue();
									if (null != incrementValue) {
										durationProduct += incrementValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_SHORT_MESSAGE.equals(productType)) {
									Long incrementValue = productInfo.getIncrementValue();
									if (null != incrementValue) {
										shortMessageProduct += incrementValue;
									}
								} else if (ProductInfo.PRODUCT_TYPE_STORAGE.equals(productType)) {
									Long incrementValue = productInfo.getIncrementValue();
									if (null != incrementValue) {
										storageProduct += incrementValue;
									}
								}
							}
						}
					}
					bean.setProductList(productList);
					bean.setConcurrenceProduct(concurrenceProduct);
					bean.setDurationProduct(durationProduct);
					bean.setShortMessageProduct(shortMessageProduct);
					bean.setStorageProduct(storageProduct);
				}
			}
		}
	}

	@Autowired
	private PackageInfoDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private ProductInfoMng productInfoMng;
	@Autowired
	private PackageAndProductMng packageAndProductMng;
}
