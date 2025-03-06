package com.bvRadio.iLive.iLive.action.admin;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.RechargeVirtualCurrencyMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * 媒体平台自充值
 * @author YanXL
 *
 */
@Controller
public class RechargeAdminController {

	@Autowired
	private RechargeVirtualCurrencyMng rechargeVirtualCurrencyMng;
	/**
	 * 跳转充值页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/recharge/getCreateType")
	public String rechargeAdminPaymentType(HttpServletRequest request, ModelMap model){
		UserBean user = ILiveUtils.getUser(request);
		model.addAttribute("user", user);
		model.addAttribute("leftActive", "5_6");
		model.addAttribute("out_trade_no", UUID.randomUUID().toString().replace("-", ""));
		model.addAttribute("topActive", "1");
		return "recharge/recharge_type";
	}
	/**
	 * 支付宝充值创建
	 * @param request
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param paymentType 支付方式   ：0：支付宝   1：微信
	 * @param user_id 充值用户ID
	 * @param userName 充值用户名称
	 * @param out_trade_no 商户订单号
	 * @param orderDescription 订单描述
	 * @return 
	 */
	@RequestMapping(value="/recharge/getCreate",method=RequestMethod.POST)
	public @ResponseBody String alipayTradeCreatePagePay(HttpServletRequest request,Integer totalAmount,Integer paymentType,Integer user_id,String userName,String out_trade_no,String orderDescription){
		String result ="";
		if(paymentType==Constants.PAYMENT_TYPE_ALIPAY){
			//支付宝 创建 未支付
			result = rechargeVirtualCurrencyMng.alipayTradePay(request,totalAmount,user_id,userName,out_trade_no,orderDescription);
		}
		return result;
	}
	/**
	 * 回调页面
	 * @param results
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/recharge/getResults")
	public String rechargeAdminResults(String results,ModelMap model){
		return results;
	}
	
//=============================================================================================================================
	/** 
	 * 微信扫码支付统一下单   交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。外币交易的支付金额精确到币种的最小单位，参数值不能带小数点
	 * @param totalAmount 充值金额  ：元 （整数）
	 * @param paymentType 支付方式   ：0：支付宝   1：微信
	 * @param user_id 充值用户ID
	 * @param userName 充值用户名称
	 * @param out_trade_no 商户订单号
	 * @param orderDescription 订单描述
	 */  
	@RequestMapping(value = "/weixin/WxPayUnifiedorder", method = RequestMethod.POST)  
	public @ResponseBody Object WxPayUnifiedorder(HttpServletRequest request,Integer totalAmount,Integer paymentType,Integer user_id,String userName,String out_trade_no,String orderDescription) throws Exception{  
		String result ="";
		if(paymentType==Constants.PAYMENT_TYPE_WEIXIN){
			//微信 创建 未支付
			result = rechargeVirtualCurrencyMng.wEIXINTradePay(request,totalAmount,user_id,userName,out_trade_no,orderDescription);
		}       
	    return result;  
	}
	/** 
	 * 生成二维码图片并直接以流的形式输出到页面 
	 * @param code_url 
	 * @param response 
	 */  
	@RequestMapping(value="/weixin/qr_codeImg")  
	@ResponseBody  
	public void getQRCode(String code_url,HttpServletResponse response){  
		rechargeVirtualCurrencyMng.encodeQrcode(code_url, response);  
	} 
	
}
