package com.jwzt.billing.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.ProductInfoDao;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.billing.manager.ProductInfoMng;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class ProductInfoMngImpl implements ProductInfoMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(String productName, Integer[] productTypes, Date startTime, Date endTime, int pageNo,
			int pageSize, boolean initBean) {
		Pagination page = dao.pageByParams(productName, productTypes, startTime, endTime, pageNo, pageSize);
		if (null != page && initBean) {
			List<ProductInfo> list = (List<ProductInfo>) page.getList();
			if (null != list) {
				for (ProductInfo bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductInfo> listByParams(String productName, Integer[] productTypes, Date startTime, Date endTime,
			boolean initBean) {
		List<ProductInfo> list = dao.listByParams(productName, productTypes, startTime, endTime);
		if (null != list && initBean) {
			for (ProductInfo bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public ProductInfo getBeanById(Integer id, boolean initBean) {
		ProductInfo bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProductInfo save(final ProductInfo bean) {
		if (null != bean) {
			Long nextId = fieldIdManagerMng.getNextId("billing_product", "id", 1L);
			if (nextId > 0) {
				bean.setId(nextId.intValue());
				bean.initFieldValue();
				dao.save(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProductInfo update(ProductInfo bean) {
		if (null != bean) {
			Updater<ProductInfo> updater = new Updater<ProductInfo>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	private void initBean(final ProductInfo bean) {
		if (null != bean) {
		}
	}

	@Autowired
	private ProductInfoDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
}
