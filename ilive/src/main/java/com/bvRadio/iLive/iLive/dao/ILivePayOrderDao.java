package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILivePayOrder;

public interface ILivePayOrderDao {
	/**
	 * 新增数据
	 * @param iLivePayOrder
	 * @throws Exception
	 */
	public void insertILivePayOrder(ILivePayOrder iLivePayOrder) throws Exception;
	/**
	 * 获取订单信息
	 * @param merOrderNo 自定义商户订单号
	 * @param orderNo 订单号    ，视讯平台唯一订单号
	 * @return
	 */
	public List<ILivePayOrder> selectILivePayOrder(String merOrderNo, String orderNo) throws Exception;
	/**
	 * 修改数据
	 * @param iLivePayOrder
	 * @return
	 */
	public void updateILivePayOrder(ILivePayOrder iLivePayOrder) throws Exception;
	/**
	 * 根据主键获取数据
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public ILivePayOrder selectILivePayOrderById(Long orderId) throws Exception;
	/**
	 * 根据条件获取订单信息
	 * @param userId 用户ID
	 * @param evenId 场次ID
	 * @param orderType 订单类型
	 * @return
	 * @throws Exception
	 */
	public List<ILivePayOrder> selectILivePayOrderByEvenIdAndUserId(Long userId,Long evenId,Integer orderType) throws Exception;

}
