package com.bvRadio.iLive.iLive.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bvRadio.iLive.iLive.entity.RedRecordingVo;
import com.bvRadio.iLive.iLive.entity.ReturnMsg;
import com.bvRadio.iLive.iLive.entity.UserBalancesBean;
import com.bvRadio.iLive.iLive.util.RedNumberVo;

/**
 * 充值  、消费 事务控制
 * @author YanXL
 *
 */
public interface RechargeVirtualCurrencyMng {
	/**
	 * 支付宝充值创建
	 * @param request
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param user_id 充值用户ID
	 * @param userName 充值用户名称
	 * @param out_trade_no 商户订单号
	 * @param orderDescription 商品描述
	 * @return
	 */
	public String alipayTradePay(HttpServletRequest request, Integer totalAmount,
			Integer user_id, String userName,String out_trade_no,String orderDescription);
	/**
	 * 微信支付创建
	 * @param request
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param user_id 充值用户ID
	 * @param userName 充值用户名称
	 * @return
	 */
	public String mchWeiXinPay(HttpServletRequest request, Integer totalAmount,
			Integer user_id, String userName);
	/**
	 * 支付结果处理
	 * @param user_id 用户ID
	 * @param paymentType  支付方式 
	 * @param userName 用户名称
	 * @param out_trade_no 商户订单号
	 * @param payment_order_number 第三方订单号
	 * @return
	 */
	public ReturnMsg alipayAliPayResults(Integer paymentType,Integer user_id,  String userName,
			String out_trade_no, String payment_order_number);
	/**
	 * 充值记录  创建
	 * @param paymentType 支付方式
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param user_id 用户ID
	 * @param userName 用户名称
	 * @param portType 前端类型
	 * @return
	 */
	public String alipayTradeCreateRechargeRecordBean(Integer paymentType,
			Integer totalAmount, Integer user_id, String userName, Integer portType);
	/**
	  * 赠送礼物
	 * @param user_id 用户ID （消费者）
	 * @param userName 用户名称
	 * @param admin_id 主播ID （直播用户ID）
	 * @param adminName 主播名称
	 * @param gift_id 礼物ID
	 * @param gift_number 礼物数量
	 * @param live_Id 直播id
	 * @return
	 */
	public ReturnMsg consumptionBuyGift(Integer user_id, String userName,
			Integer admin_id, String adminName, Integer gift_id,
			Integer gift_number, Integer live_Id);
	/**
	 * 根据用户ID获取账户信息
	 * @param user_id 用户ID
	 * @param userName 用户名称
	 * @return
	 */
	public UserBalancesBean getUserBalancesByUserID(Integer user_id, String userName);
	/**
	 * 支付宝支付回调 服务器异步通知页面路径
	 * @param request
	 * @param response
	 * @return
	 */
	public String alipayNotifyUrl(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 支付宝支付回调 页面跳转同步通知页面路径
	 * @param request
	 * @param response
	 */
	public String alipayReturnUrl(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 微信预支付创建
	 * @param request
	 * @param totalAmount 金额
	 * @param user_id 用户id
	 * @param userName 用户名称
	 * @param out_trade_no 商户订单号
	 * @param orderDescription 商品描述
	 * @return
	 */
	public String wEIXINTradePay(HttpServletRequest request,
			Integer totalAmount, Integer user_id, String userName,
			String out_trade_no, String orderDescription);
	/**
	 * 二维码生成
	 * @param code_url
	 * @param response
	 */
	public void encodeQrcode(String code_url, HttpServletResponse response);
	/**
	 * 提现请求处理
	 * @param user_id 用户ID
	 * @param user_name 用户名称
	 * @param commission_income 提取收益额度（虚拟币数量）
	 * @param presentType 提现账户类型：0 支付宝，1 微信
	 * @param present_account 账户
	 * @param real_name 账户真实姓名
	 * @return
	 */
	public ReturnMsg addPresentRecordBeanByIncomePresent(Integer user_id,
			String user_name, double commission_income, Integer presentType,
			String present_account, String real_name);
	/**
	 * 个人 支付宝充值创建
	 * @param request
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param user_id 充值用户ID
	 * @param userName 充值用户名称
	 * @param out_trade_no 商户订单号
	 * @param orderDescription 商品描述
	 * @return
	 */
	public String alipayTradePayPersonal(HttpServletRequest request,
			Integer totalAmount, Integer user_id, String userName,
			String out_trade_no, String orderDescription);
	/**
	 * 苹果支付处理
	 * @param receiptData 加密字符串
	 * @param type 0代表开发环境 1代表正式上线环境
	 * @param userId 用户ID
	 * @return
	 */
	public ReturnMsg applePayResults(String receiptData, Integer type,
			Integer userId);
	/**
	 * 抢红包接口
	 * @param response
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param liveId 直播间ID
	 * @param parentsId 领取红包主体  （请红包时为所抢红包主体ID  ， 送红包时0）
	 * @param userImg 用户头像
	 * @return
	 */
	public String addRedRecordingBean( Integer userId,
			String userName, Integer liveId,
			Integer parentsId, String userImg);
	/**
	 * 获取所有抢红包数据
	 * @param redId
	 * @return
	 */
	public RedRecordingVo getRedRecordingBeanByParentsId(Integer redId);
	/**
	 * 送红包接口
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param redAmount 发送红包金额
	 * @param redNumber 红包个数
	 * @param liveId  直播间ID
	 * @param userImg  用户头像
	 * @return
	 */
	public RedNumberVo outRedRecordingBean(Integer userId, String userName,
			long redAmount, Integer redNumber, Integer liveId, String userImg);
	/**
	 * 退回红包接口
	 * @param response
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param redAmount 退回红包总金额
	 * @param parentsId 退回红包主体  （请红包时为所抢红包主体ID  ， 送红包时0）
	 * @param redNumber 数量
	 * @return
	 */
	public ReturnMsg backRedRecordingBean(Integer userId, String userName,
			long redAmount, Integer parentsId, Integer redNumber);
	
}
