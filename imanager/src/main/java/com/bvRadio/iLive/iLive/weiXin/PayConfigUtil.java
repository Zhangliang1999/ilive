package com.bvRadio.iLive.iLive.weiXin;

public class PayConfigUtil {
	//微信分配的公众号ID（开通公众号之后可以获取到）
		public static String APP_ID="";//微信开发平台应用ID
		
		//public static String APP_SECRET="";//应用凭证
		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）  
		public static String MCH_ID="";//商户号
		
		//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）  
	    // 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证  
	    // 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改 
		public static String API_KEY="";//私有key
		
		 //1）被扫支付API
		public static String UFDODER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";//微信开发拼台应用ID
		 
		//回调地址  
		public static String NOTIFY_URL="http://192.168.1.215:8787/bvRadioILive/alipay/getReturn.jspx";//回掉地址
		
		public static String CREATE_IP="192.168.1.215";//发起支付ip
		  
	    //2）被扫支付查询API  
	    public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";  
	  
	    //3）退款API  
	    public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";  
	  
	    //4）退款查询API  
	    public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";  
	  
	    //5）撤销API  
	    public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";  
	  
	    //6）下载对账单API  
	    public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";  
	  
	    //7) 统计上报API  
	    public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";  
}
