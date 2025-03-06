package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILivePackageAndProduct;

public interface ILivePackageAndProductDao {

	public ILivePackageAndProduct getMsgBypackageId(Integer packageId) ;

	public ILivePackageAndProduct getMsgByProductId(Integer productId);

}
