package com.bvRadio.iLive.iLive.manager;



import com.bvRadio.iLive.iLive.entity.ILivePackageAndProduct;

public interface ILivePackageAndProductMng {
	

	public ILivePackageAndProduct getMsgBypackageId(Integer packageId);
	public ILivePackageAndProduct getMsgByProductId(Integer ProductId);
}
