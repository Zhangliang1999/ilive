package com.bvRadio.iLive.test;

import com.bvRadio.iLive.iLive.util.Base64;

import com.bvRadio.iLive.iLive.util.HMACSHA1;

import com.jwzt.common.Md5Util;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicLong;


public class creatSignTest {
public static void main(String[] args) {
//	String key=Md5Util.encode("TYSXMSG");
//	String QueryString = "";
//	
////	String stringToSign = QueryString.substring(1);
//	String stringToSign="123";
//	String singature=null;
//	//HMAC-SHA1 加密
//	String hmacSign;
//	try {
//		hmacSign = HMACSHA1.getSignature(stringToSign, key+"&");
//		System.out.println("stringToSign:"+stringToSign);
//		System.out.println("key:"+key);
//		System.out.println("hmacSign:"+hmacSign);
//		singature =Base64.encode(hmacSign,"UTF-8");
////		singature=singature.substring(2, 32);
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	//System.out.println(singature+"长度："+singature.length());
//	String url="http://apizb.tv189.com/ilive/appuser/postformdata.jspx?formId=1232&callback=jQuery1720015293500779969937_1560909446341&userId=null&diyFormData=%5B%7B%22needMsgValid%22%3A%221%22%2C%22subValue%22%3A%22%22%2C%22dataOrder%22%3A%220%22%2C%22dataValue%22%3A%2218510328589%22%2C%22dataTitle%22%3A%22%5Cn%5Ct%5Ct%5Ct%E6%89%8B%E6%9C%BA%E9%AA%8C%E8%AF%81%5Cn%5Ct%5Ct%22%2C%22dataModelId%22%3A%225748%22%7D%2C%7B%22needMsgValid%22%3A0%2C%22subValue%22%3A%22%22%2C%22dataValue%22%3A%222167%22%7D%5D&_=1560909571098";
//	try {
//		String deurl = URLDecoder.decode(url,"UTF-8");
//		System.out.println(deurl);
//	} catch (UnsupportedEncodingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	AtomicLong count = new AtomicLong();
	Double order = (double) (System.currentTimeMillis() / 1000L);
	Double order1 = (double) (System.currentTimeMillis() + 10104 + count.incrementAndGet() / 1000L);
	System.out.println("batchAddHistory==========="+order);
	System.out.println("saveVideoHistory==========="+order1);
}
}
