package com.bvRadio.iLive.iLive.manager.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILivePackageAndProductDao;

import com.bvRadio.iLive.iLive.entity.ILivePackageAndProduct;
import com.bvRadio.iLive.iLive.manager.ILivePackageAndProductMng;

@Transactional
@Service
public class ILivePackageAndProductMngImp implements ILivePackageAndProductMng {
   @Autowired  ILivePackageAndProductDao iLivePackageAndProductDao; 

	@Override
	public ILivePackageAndProduct getMsgBypackageId(Integer packageId) {
		// TODO Auto-generated method stub
		return iLivePackageAndProductDao.getMsgBypackageId(packageId);
	}

	@Override
	public ILivePackageAndProduct getMsgByProductId(Integer ProductId) {
		// TODO Auto-generated method stub
		return iLivePackageAndProductDao.getMsgByProductId(ProductId);
	}
	

}
