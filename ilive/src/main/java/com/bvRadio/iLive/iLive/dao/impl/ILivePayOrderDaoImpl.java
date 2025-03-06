package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILivePayOrderDao;
import com.bvRadio.iLive.iLive.entity.ILivePayOrder;

public class ILivePayOrderDaoImpl extends HibernateBaseDao<ILivePayOrder, Long>  implements ILivePayOrderDao {

	@Override
	public void insertILivePayOrder(ILivePayOrder iLivePayOrder)
			throws Exception {
		getSession().save(iLivePayOrder);

	}

	@Override
	protected Class<ILivePayOrder> getEntityClass() {
		return ILivePayOrder.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILivePayOrder> selectILivePayOrder(String merOrderNo, String orderNo)
			throws Exception {
		Finder finder = Finder.create("from ILivePayOrder bean");
		finder.append(" where bean.orderNumber=:orderNumber and bean.tradeNo=:tradeNo ");
		finder.setParam("orderNumber", merOrderNo);
		finder.setParam("tradeNo", orderNo);
		List<ILivePayOrder> find = find(finder);
		return find;
	}

	@Override
	public void updateILivePayOrder(ILivePayOrder iLivePayOrder)
			throws Exception {
		getSession().update(iLivePayOrder);
	}

	@Override
	public ILivePayOrder selectILivePayOrderById(Long orderId) throws Exception {
		return get(orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILivePayOrder> selectILivePayOrderByEvenIdAndUserId(
			Long userId, Long evenId, Integer orderType) throws Exception {
		Finder finder = Finder.create("from ILivePayOrder bean");
		finder.append(" where bean.userId=:userId and bean.evenId=:evenId and bean.orderType=:orderType ");
		finder.setParam("userId", userId);
		finder.setParam("evenId", evenId);
		finder.setParam("orderType", orderType);
		List<ILivePayOrder> find = find(finder);
		return find;
	}

}
