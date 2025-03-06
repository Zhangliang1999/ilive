package com.bvRadio.iLive.iLive.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;

/**
 * 支付宝
 * 
 * @author Administrator
 * 
 */
public class AlipayConfig {

	/**
	 * 支付宝支付
	 * 
	 * @param request
	 * @param totalAmount
	 *            支付金额
	 * @param out_trade_no
	 *            商户订单号
	 * @param subject
	 *            订单名称
	 * @param body
	 *            商品描述
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public static String alipayTradePagePay(HttpServletRequest request,
			String totalAmount, String out_trade_no, String subject, String body)
			throws Exception {
		// 支付宝网关
		String gatewayUrl = SystemXMLTomcatUrl.getUrl("gatewayUrl");
		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		String app_id = SystemXMLTomcatUrl.getUrl("APPID");
		// 商户私钥，您的PKCS8格式RSA2私钥
		String merchant_private_key = SystemXMLTomcatUrl
				.getUrl("MERCHANT_PRIVATE_KEY");
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		String notify_url = SystemXMLTomcatUrl.getUrl("NOTIFY_URL");
		// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		String return_url = SystemXMLTomcatUrl.getUrl("RETURN_URL");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");
		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id,
				merchant_private_key, "json", charset, alipay_public_key,
				sign_type);
		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(return_url);
		alipayRequest.setNotifyUrl(notify_url);
		body = new String(body.getBytes("ISO-8859-1"), "UTF-8");
		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no
				+ "\"," + "\"total_amount\":\"" + totalAmount + "\","
				+ "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body
				+ "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		// 请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		System.out.println(result);
		return result;
	}

	/* *
	 * 功能：支付宝服务器异步通知页面 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	 * ************************页面功能说明*************************
	 * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。 如果没有收到该页面返回的 success
	 * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
	 */
	@SuppressWarnings("unchecked")
	public static boolean alipayNotifyUrl(HttpServletRequest request)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");
		boolean signVerified = AlipaySignature.rsaCheckV1(params,
				alipay_public_key, charset, sign_type); // 调用SDK验证签名
		return signVerified;
	}

	/* *
	 * 功能：支付宝服务器同步通知页面 说明：
	 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	 * ************************页面功能说明*************************
	 * 该页面仅做页面展示，业务逻辑处理请勿在该页面执行
	 */
	@SuppressWarnings("unchecked")
	public static boolean alipayReturnUrl(HttpServletRequest request)
			throws Exception {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			System.out.println("MAP_KEY:" + name);
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				System.out.println("MAP_VALUES:" + values[i]);
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");
		boolean signVerified = AlipaySignature.rsaCheckV1(params,
				alipay_public_key, charset, sign_type); // 调用SDK验证签名
		return signVerified;
	}

	/**
	 * 退款
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String alipayRefund(HttpServletRequest request)
			throws Exception {
		// 支付宝网关
		String gatewayUrl = SystemXMLTomcatUrl.getUrl("gatewayUrl");
		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		String app_id = SystemXMLTomcatUrl.getUrl("APPID");
		// 商户私钥，您的PKCS8格式RSA2私钥
		String merchant_private_key = SystemXMLTomcatUrl
				.getUrl("MERCHANT_PRIVATE_KEY");
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id,
				merchant_private_key, "json", charset, alipay_public_key,
				sign_type);
		// 设置请求参数
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		// 商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = new String(request.getParameter("out_trade_no")
				.getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes(
				"ISO-8859-1"), "UTF-8");
		// 请二选一设置
		// 需要退款的金额，该金额不能大于订单金额，必填
		String refund_amount = new String(request.getParameter("total_amount")
				.getBytes("ISO-8859-1"), "UTF-8");

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no
				+ "\"," + "\"trade_no\":\"" + trade_no + "\","
				+ "\"refund_amount\":\"" + refund_amount + "\"}");
		// 请求
		String result = alipayClient.execute(alipayRequest).getBody();
		return result;
	}

	/**
	 * 支付宝转账 商家转账到个人用户上
	 * 
	 * @param out_biz_no
	 *            商户转账唯一订单号
	 * @param payee_account
	 *            收款方账户：支付宝登录账号
	 * @param amount
	 *            转账金额，单位：元。
	 * @param payee_real_name
	 *            收款方真实姓名 ：如果本参数不为空，则会校验该账户在支付宝登记的实名是否与收款方真实姓名一致
	 * @return
	 * @throws Exception
	 */
	public static AlipayFundTransToaccountTransferResponse transToaccountTransfer(
			String out_biz_no, String payee_account, String amount,
			String payee_real_name) throws Exception {
		String gatewayUrl = SystemXMLTomcatUrl.getUrl("gatewayUrl");
		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		String app_id = SystemXMLTomcatUrl.getUrl("APPID");
		// 商户私钥，您的PKCS8格式RSA2私钥
		String merchant_private_key = SystemXMLTomcatUrl
				.getUrl("MERCHANT_PRIVATE_KEY");
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");

		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id,
				merchant_private_key, "json", charset, alipay_public_key,
				sign_type);
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" + "\"out_biz_no\":\"" + out_biz_no + "\","
				+ "\"payee_type\":\"ALIPAY_LOGONID\"," + "\"payee_account\":\""
				+ payee_account + "\"," + "\"amount\":\"" + amount + "\","
				+ "\"payer_show_name\":\"直播平台提现\"," + "\"payee_real_name\":\""
				+ payee_real_name + "\"," + "\"remark\":\"支付宝提现\"" + "}");
		AlipayFundTransToaccountTransferResponse response = alipayClient
				.execute(request);
		return response;
	}

	/**
	 * 个人使用支付宝支付
	 * 
	 * @param request
	 * @param totalAmount
	 *            支付金额
	 * @param out_trade_no
	 *            商户订单号
	 * @param subject
	 *            订单名称
	 * @param body
	 *            商品描述
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public static String alipayTradePagePayPersonal(HttpServletRequest request,
			String totalAmount, String out_trade_no, String subject, String body)
			throws Exception {
		// 支付宝网关
		String gatewayUrl = SystemXMLTomcatUrl.getUrl("gatewayUrl");
		// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		String app_id = SystemXMLTomcatUrl.getUrl("APPID");
		// 商户私钥，您的PKCS8格式RSA2私钥
		String merchant_private_key = SystemXMLTomcatUrl
				.getUrl("MERCHANT_PRIVATE_KEY");
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
		// 对应APPID下的支付宝公钥。
		String alipay_public_key = SystemXMLTomcatUrl
				.getUrl("ALIPAY_PUBLIC_KEY");
		// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		String notify_url = SystemXMLTomcatUrl.getUrl("NOTIFY_URL_tlap");
		// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		String return_url = SystemXMLTomcatUrl.getUrl("RETURN_URL_tlap");
		// 签名方式
		String sign_type = SystemXMLTomcatUrl.getUrl("sign_type");
		// 字符编码格式
		String charset = SystemXMLTomcatUrl.getUrl("charset");
		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id,
				merchant_private_key, "json", charset, alipay_public_key,
				sign_type);
		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(return_url);
		alipayRequest.setNotifyUrl(notify_url);
		body = new String(body.getBytes("ISO-8859-1"), "UTF-8");
		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no
				+ "\"," + "\"total_amount\":\"" + totalAmount + "\","
				+ "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body
				+ "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		// 请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		System.out.println(result);
		return result;
	}
}
