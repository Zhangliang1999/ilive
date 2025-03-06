package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILivePayOrder;
/**
 * 订单处理
 * @author YanXL
 *
 */
public interface ILivePayOrderMng {
	/**
	 * 新增数据
	 * @param iLivePayOrder
	 * @return 
	 * @throws Exception
	 */
	public Long addILivePayOrder(ILivePayOrder iLivePayOrder) throws Exception;
	/**
	 * 获取订单信息
	 * @param merOrderNo 自定义商户订单号
	 * @param orderNo 订单号    ，视讯平台唯一订单号
	 * @return
	 */
	public ILivePayOrder selectILivePayOrder(String merOrderNo, String orderNo) throws Exception;
	/**
	 * 修改数据
	 * @param iLivePayOrder
	 * @return
	 */
	public boolean updateILivePayOrder(ILivePayOrder iLivePayOrder);
	/**
	 * 根据主键ID获取数据
	 * @param orderId 订单ID
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
	public boolean selectILivePayOrderByEvenIdAndUserId(Long userId,Long evenId,Integer orderType) throws Exception;
}
