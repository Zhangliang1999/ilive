package com.bvRadio.iLive.iLive.dao.impl;


import org.springframework.stereotype.Repository;


import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILivePackageAndProductDao;


import com.bvRadio.iLive.iLive.entity.ILivePackageAndProduct;

@Repository
public class ILivePackageAndProductDaoImp  extends HibernateBaseDao<ILivePackageAndProduct, Integer> implements  ILivePackageAndProductDao{
	@Override
	public ILivePackageAndProduct getMsgBypackageId(Integer packageId) {
		if (packageId == null) {
			return null;
		}
		return (ILivePackageAndProduct) this.findUnique("from ILivePackageAndProduct where LHpackageId=?", packageId);
	
	}
	@Override
	public ILivePackageAndProduct getMsgByProductId(Integer productId) {
		if (productId == null) {
			return null;
		}
		return (ILivePackageAndProduct) this.findUnique("from ILivePackageAndProduct where ProductId=?", productId);
	
	}
	@Override
	protected Class<ILivePackageAndProduct> getEntityClass() {
		// TODO Auto-generated method stub
		return ILivePackageAndProduct.class;
	}
	
	
}
