package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.RequestUtils;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.PayOrderUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILivePayOrder;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILivePayOrderMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
/**
 * 微信  
 * @author YanXL
 *
 */
@RequestMapping(value="wx")
@Controller
public class ILiveWXPayController {
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILivePayOrderMng iLivePayOrderMng;
	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 创建订单
	 * @param contentId 内容ID
	 * @param productDesc 商品描述
	 * @param totalAmount 金额（分）
	 * @param userId 用户ID
	 * @param orderType 订单类型
	 * @param roomId 直播间
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/create/order.jspx")
	public void wechatUnifyTheOrder(Long contentId,String terminalType,String terminalOrderType,Integer orderType,Integer roomId,String productDesc,String totalAmount,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		String params="";
		try {
			long liveEventId = 0;
			if(orderType==ILivePayOrder.ORDER_TYPE_MEDIA){
				ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(contentId);
				Long enterpriseId = iLiveMediaFile.getEnterpriseId();
				roomId = iLiveMediaFile.getLiveRoomId();
				liveEventId = iLiveMediaFile.getLiveEventId()==null?0:iLiveMediaFile.getLiveEventId();
				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId.intValue());
				Integer certStatus = iLiveEnterprise.getCertStatus();
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_VodWatch,certStatus);
				if(b){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_VodWatch_Content);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}else{
				ILiveLiveRoom findById = iLiveLiveRoomMng.findById(roomId);
				if(findById!=null){
					liveEventId = findById.getLiveEvent().getLiveEventId()==null?0:findById.getLiveEvent().getLiveEventId();
					if(orderType==ILivePayOrder.ORDER_TYPE_GIFT||orderType==ILivePayOrder.ORDER_TYPE_PLAY_REWARDS){
						ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(findById.getEnterpriseId());
						Integer certStatus = iLiveEnterprise.getCertStatus();
						boolean b = EnterpriseUtil.selectIfEn(findById.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_PlayGift,certStatus);
						if(b){
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_PlayGift_Content);
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}
					}
				}
			}
			ILivePayOrder iLivePayOrder = new ILivePayOrder();
			iLivePayOrder.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			iLivePayOrder.setPayStatus(ILivePayOrder.WX_PAY_STATUS_NO);
			iLivePayOrder.setUserId(userId);
			Map<String, String> map = new HashMap<String, String>();
			//商户订单号，第三方订单必填	N
			String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			String account = iLiveManager.getMobile();
			iLivePayOrder.setUserName(iLiveManager.getNailName());
			String amount = totalAmount;
			params = params+ "account="+account;
			map.put("account", account);
			params = params+ "&amount="+amount;
			map.put("amount", amount);
			//应用编号	Y
			String appId = ConfigUtils.get("wx_appId");
			params = params+"&appId="+appId;
			map.put("appId", appId);
			//内容ID，没有内容ID，就传0	Y
			params = params+ "&contentId=0";
			map.put("contentId", "0");
			//商户编号	Y
			String merCode = ConfigUtils.get("wx_merCode");
			params = params+"&merCode="+merCode;
			map.put("merCode", merCode);
			String merOrderNo = merCode + today + createCode(8);
			iLivePayOrder.setOrderNumber(merOrderNo);
			params = params+ "&merOrderNo="+merOrderNo;
			map.put("merOrderNo", merOrderNo);
			//商品描述	N
			productDesc =  URLDecoder.decode((new String(productDesc.getBytes("ISO8859-1"), "UTF-8")), "UTF-8");
			//productDesc = URLDecoder.decode(productDesc, "UTF-8");
			params = params+ "&productDesc="+productDesc;
			iLivePayOrder.setPayDesc(productDesc);
			map.put("productDesc", productDesc);
			//产品包ID	N
			String productId = ConfigUtils.get("wx_productId");	
			params = params+ "&productId="+productId;
			map.put("productId", productId);
			//产品包名称	Y
			String productName = ConfigUtils.get("wx_productName");	
			params = params+ "&productName="+productName;
			map.put("productName", productName);
			//推广渠道	Y
			String promotionChannel = "1";
			params = params+ "&promotionChannel="+promotionChannel;
			map.put("promotionChannel", promotionChannel);
			//应用编号,即appid	Y
			String roamType = appId;
			params = params+ "&roamType="+roamType;
			map.put("roamType", roamType);
			//订单来源 1：普通产品订单    7：直播互动订单    8：VIP商城订单   10：第三方订单	Y
			params = params+ "&source=10";
			map.put("source", "10");
			//订购数量	Y
			params = params+ "&subCount=1";
			map.put("subCount", "1");
			//订单总金额	Y
			terminalOrderType = terminalOrderType==null?"android":terminalOrderType;
			params = params+ "&ternimalType="+terminalOrderType;
			params = params+ "&totalAmount="+totalAmount;
			map.put("totalAmount", totalAmount);
			//支付金额(分)	Y
			iLivePayOrder.setAmount(Long.valueOf(amount));
			//统一用户UID或合作方用户ID	Y
			params = params+ "&userId="+userId;
			map.put("userId", userId+"");
			String userName = iLiveManager.getNailName();
			params = params+ "&userName="+userName;
			map.put("userName", userName);
			params = params+ "&wxAppId="+appId;
			map.put("wxAppId", appId);
			String wxMchId = ConfigUtils.get("wx_mchId");
			params = params+ "&wxMchId="+wxMchId;
			map.put("wxMchId", wxMchId);
			String wxTradeType = "APP";
			params = params+ "&wxTradeType="+wxTradeType;
			map.put("wxTradeType", wxTradeType);
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			params = params+ "&time="+time+ConfigUtils.get("wx_key");
			map.put("time", time);
			String sign = DigestUtils.md5Hex(params.getBytes("UTF-8")).toLowerCase();
			map.put("sign", sign);
			map.put("ternimalType", terminalOrderType);
			System.out.println("params："+params);
			String url =ConfigUtils.get("wx_create_order");	
			System.out.println("开始请求远程地址："+new Date().getTime());
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			System.out.println("请求远程地址结果响应成功："+new Date().getTime());
			JSONObject jsonObject = new JSONObject(postJson);
			String code = (String) jsonObject.get("code");
			if(code.equals("0")){
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", (String)jsonObject.get("msg"));
				resultJson.put("appId", appId);
				resultJson.put("partnerid", wxMchId);
				resultJson.put("prepayid", (String)jsonObject.get("prePayId"));
				resultJson.put("package", (String)jsonObject.get("packageValue"));
				resultJson.put("noncestr", (String)jsonObject.get("nonceStr"));
				resultJson.put("timestamp", (String)jsonObject.get("timeStamp"));
				resultJson.put("sign", (String)jsonObject.get("sign"));
				resultJson.put("orderNo", (String)jsonObject.get("orderNo"));
				iLivePayOrder.setTradeNo((String)jsonObject.get("orderNo"));
				iLivePayOrder.setOrderType(orderType);
				iLivePayOrder.setRoomId(roomId);
				iLivePayOrder.setEvenId(liveEventId);
				contentId = contentId==null?0:contentId;
				iLivePayOrder.setContentId(contentId);
				Long orderId = iLivePayOrderMng.addILivePayOrder(iLivePayOrder);
				resultJson.put("orderId", orderId);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}else{
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", (String)jsonObject.get("msg"));
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}
		} catch (Exception e) {
			System.out.println("下单异常发生："+e.toString());
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "下单异常");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}
	/**
	 * H5 PC 微信支付二维码生成 订单
	 * @param contentId 内容ID
	 * @param productDesc 商品描述
	 * @param totalAmount 金额（分）
	 * @param userId 用户ID
	 * @param orderType 订单类型
	 * @param roomId 直播间
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sweep/order.jspx")
	public void wechatSweepUnifyTheOrder(Long contentId,String terminalType,String terminalOrderType,Integer orderType,Integer roomId,String productDesc,String totalAmount,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		String params="";
		try {
			long liveEventId = 0;
			if(orderType==ILivePayOrder.ORDER_TYPE_MEDIA){
				ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(contentId);
				Long enterpriseId = iLiveMediaFile.getEnterpriseId();
				liveEventId = iLiveMediaFile.getLiveEventId();
				roomId = iLiveMediaFile.getLiveRoomId();
				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId.intValue());
				Integer certStatus = iLiveEnterprise.getCertStatus();
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_VodWatch,certStatus);
				if(b){
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_VodWatch_Content);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			}else{
				ILiveLiveRoom findById = iLiveLiveRoomMng.findById(roomId);
				if(findById!=null){
					liveEventId = findById.getLiveEvent().getLiveEventId();
					if(orderType==ILivePayOrder.ORDER_TYPE_GIFT||orderType==ILivePayOrder.ORDER_TYPE_PLAY_REWARDS){
						ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(findById.getEnterpriseId());
						Integer certStatus = iLiveEnterprise.getCertStatus();
						boolean b = EnterpriseUtil.selectIfEn(findById.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_PlayGift,certStatus);
						if(b){
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_PlayGift_Content);
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}
					}
				}
			}
			ILivePayOrder iLivePayOrder = new ILivePayOrder();
			iLivePayOrder.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			iLivePayOrder.setPayStatus(ILivePayOrder.WX_PAY_STATUS_NO);
			iLivePayOrder.setUserId(userId);
			Map<String, String> map = new HashMap<String, String>();
			//商户订单号，第三方订单必填	N
			String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			String account = iLiveManager.getMobile();
			iLivePayOrder.setUserName(iLiveManager.getNailName());
			String amount = totalAmount;
			params = params+ "account="+account;
			map.put("account", account);
			params = params+ "&amount="+amount;
			map.put("amount", amount);
			//应用编号	Y
			String appId = ConfigUtils.get("wx_appId");
			params = params+"&appId="+appId;
			map.put("appId", appId);
			//内容ID，没有内容ID，就传0	Y
			params = params+ "&contentId=0";
			map.put("contentId", "0");
			//商户编号	Y
			String merCode = ConfigUtils.get("wx_merCode");
			params = params+"&merCode="+merCode;
			map.put("merCode", merCode);
			String merOrderNo = merCode + today + createCode(8);
			iLivePayOrder.setOrderNumber(merOrderNo);
			params = params+ "&merOrderNo="+merOrderNo;
			map.put("merOrderNo", merOrderNo);
			//商品描述	N
			if(terminalType.equals("h5")||terminalType.equals("H5")||terminalType.equals("PC")||terminalType.equals("pc")){
				productDesc = URLDecoder.decode(productDesc, "UTF-8");
			}else{
				productDesc =  URLDecoder.decode((new String(productDesc.getBytes("ISO8859-1"), "UTF-8")), "UTF-8");
			}
			params = params+ "&productDesc="+productDesc;
			iLivePayOrder.setPayDesc(productDesc);
			map.put("productDesc", productDesc);
			//产品包ID	N
			String productId = ConfigUtils.get("wx_productId");	
			params = params+ "&productId="+productId;
			map.put("productId", productId);
			//产品包名称	Y
			String productName = ConfigUtils.get("wx_productName");	
			params = params+ "&productName="+productName;
			map.put("productName", productName);
			//推广渠道	Y
			String promotionChannel = "1";
			params = params+ "&promotionChannel="+promotionChannel;
			map.put("promotionChannel", promotionChannel);
			//应用编号,即appid	Y
			String roamType = appId;
			params = params+ "&roamType="+roamType;
			map.put("roamType", roamType);
			//订单来源 1：普通产品订单    7：直播互动订单    8：VIP商城订单   10：第三方订单	Y
			params = params+ "&source=10";
			map.put("source", "10");
			//订购数量	Y
			params = params+ "&subCount=1";
			map.put("subCount", "1");
			terminalOrderType = terminalOrderType==null?"android":terminalOrderType;
			params = params+ "&ternimalType="+terminalOrderType;
			//订单总金额	Y
			params = params+ "&totalAmount="+totalAmount;
			map.put("totalAmount", totalAmount);
			//支付金额(分)	Y
			iLivePayOrder.setAmount(Long.valueOf(amount));
			//统一用户UID或合作方用户ID	Y
			params = params+ "&userId="+userId;
			map.put("userId", userId+"");
			String userName = iLiveManager.getNailName();
			params = params+ "&userName="+userName;
			map.put("userName", userName);
			params = params+ "&wxAppId="+appId;
			map.put("wxAppId", appId);
			String wxMchId = ConfigUtils.get("wx_mchId");
			params = params+ "&wxMchId="+wxMchId;
			map.put("wxMchId", wxMchId);
			String wxTradeType = "NATIVE";
			params = params+ "&wxTradeType="+wxTradeType;
			map.put("wxTradeType", wxTradeType);
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			params = params+ "&time="+time+ConfigUtils.get("wx_key");
			map.put("time", time);
			String sign = DigestUtils.md5Hex(params.getBytes("UTF-8")).toLowerCase();
			map.put("sign", sign);
			map.put("ternimalType", terminalOrderType);
			System.out.println("params："+params);
			String url =ConfigUtils.get("wx_code_order");	
			System.out.println("开始请求远程地址："+new Date().getTime());
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			System.out.println("请求远程地址结果响应成功："+new Date().getTime());
			JSONObject jsonObject = new JSONObject(postJson);
			String code = (String) jsonObject.get("code");
			String msg = (String) jsonObject.get("msg");
			if(code.equals("0")){
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", msg);
				String orderNo = (String) jsonObject.get("orderNo");
				String codeUrl = (String) jsonObject.get("codeUrl");//二维码URL	N，下单成功返回
				codeUrl = codeUrl.replace("\u003d", "=");
				resultJson.put("codeUrl", codeUrl);
				iLivePayOrder.setTradeNo(orderNo);
				iLivePayOrder.setOrderType(orderType);
				iLivePayOrder.setRoomId(roomId);
				iLivePayOrder.setEvenId(liveEventId);
				contentId = contentId==null?0:contentId;
				iLivePayOrder.setContentId(contentId);
				Long orderId = iLivePayOrderMng.addILivePayOrder(iLivePayOrder);
				resultJson.put("orderId", orderId);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}else{
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", msg);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}
		} catch (Exception e) {
			System.out.println("下单异常发生："+e.toString());
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "下单异常");
			ResponseUtils.renderJson(request, response, resultJson.toString());
			e.printStackTrace();
		}
	}
	/**
	 * 支付成功通知
	 * @param orderNo	订单号，视讯平台唯一订单号
	 * @param merOrderNo	商户订单号
	 * @param merCode	商户代码
	 * @param appId	应用编号
	 * @param totalAmount	订单总金额
	 * @param payTime	支付时间
	 * @param contentName	内容名称
	 * @param contentId	商品编号
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/pay/callback.jspx")
	public void payReturn(String orderNo, String merOrderNo, String merCode, String appId, String totalAmount,
			String payTime, String contentName, String contentId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		System.out.println("支付成功通知orderNo：" + orderNo + "merOrderNo：" + merOrderNo);
		try {
			if (merOrderNo == null || orderNo == null) {
				resultJson.put("code", 1);
				resultJson.put("msg", "订单号或商户订单为空");
			}
			ILivePayOrder iLivePayOrder = iLivePayOrderMng.selectILivePayOrder(merOrderNo, orderNo);
			System.out.println("查询订单数据:" + iLivePayOrder);
			if (iLivePayOrder == null) {
				System.out.println("查询订单数据为空");
				resultJson.put("code", 1);
				resultJson.put("msg", "订单查询失败，未获得订单信息");
			} else {
				iLivePayOrder.setPayStatus(ILivePayOrder.WX_PAY_STATUS_YES);
				iLivePayOrder.setEndTime(Timestamp.valueOf(format.format(new Date())));
				boolean b = iLivePayOrderMng.updateILivePayOrder(iLivePayOrder);
				Integer roomId = iLivePayOrder.getRoomId();
				System.out.println("修改数据状态：" + b);
				if (b) {
					resultJson.put("code", 0);
					resultJson.put("msg", "订单支付状态修改成功");
					Hashtable<Integer, List<ILivePayOrder>> orderListMap = PayOrderUtil.getOrderListMap();
					List<ILivePayOrder> list = orderListMap.get(roomId);
					if (list == null) {
						list = new ArrayList<ILivePayOrder>();
					}
					iLivePayOrder.setOrderUp(ILivePayOrder.ORDER_UP_NO);
					list.add(iLivePayOrder);
					orderListMap.put(roomId, list);
				} else {
					resultJson.put("code", 1);
					resultJson.put("msg", "订单支付状态修改失败");
				}
				try {
					Integer orderType = iLivePayOrder.getOrderType();
					Long userId = iLivePayOrder.getUserId();
					Long evenId = iLivePayOrder.getEvenId();
					Long fileId = iLivePayOrder.getContentId();
					Long amount = iLivePayOrder.getAmount();
					Double money = amount / 100.0;
					String ipAddr = RequestUtils.getIpAddr(request);
					if (ILivePayOrder.ORDER_TYPE_GIFT.equals(orderType)) {
						ILiveUserViewStatics.INSTANCE.sendGift(String.valueOf(userId), roomId, evenId, fileId, ipAddr,
								null, money, 1);
					} else if (ILivePayOrder.ORDER_TYPE_PLAY_REWARDS.equals(orderType)) {
						ILiveUserViewStatics.INSTANCE.reward(String.valueOf(userId), roomId, evenId, fileId, ipAddr,
								null, money);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			System.out.println("支付成功通知异常 ：" + e.toString());
			resultJson.put("code", 1);
			resultJson.put("msg", "处理异常");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 查询订单是否支付
	 * @param orderId 订单ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/select/getOrderId.jspx")
	public void selectILivePayOrder(Long orderId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if(orderId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "订单ID为空");
			}
			ILivePayOrder iLivePayOrder = iLivePayOrderMng.selectILivePayOrderById(orderId);
			if(iLivePayOrder!=null){
				resultJson.put("code", SUCCESS_STATUS);
				Integer payStatus = iLivePayOrder.getPayStatus();
				Integer orderType = iLivePayOrder.getOrderType();
				if(payStatus==ILivePayOrder.WX_PAY_STATUS_YES){
					resultJson.put("message", "订单已支付");
				}else{
					resultJson.put("message", "订单未支付");
				}
				resultJson.put("payStatus", payStatus);
				resultJson.put("orderType", orderType);
			}else{
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取信息失败");
			}
		} catch (Exception e) {
			System.out.println("支付成功通知异常 ："+e.toString());
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "处理异常");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
     * 生成6位或10位随机数 param codeLength(多少位)
     * @return
     */
    public static String createCode(int codeLength) {
        String code = "";
        for (int i = 0; i < codeLength; i++) {
            code += (int) (Math.random() * 9);
        }
        return code;
    }
}
