package com.bvRadio.iLive.iLive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


public class HttpRequest {

	public static String sendPost(String url,String param,String Authorization) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象     
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// 打开和URL之间的连接
			URL realUrl = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
			conn.setSSLSocketFactory(ssf);

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("content-Type", "application/json");
			 conn.setRequestProperty("user-agent",
			"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			 if(Authorization!=null) {
				 conn.setRequestProperty("Authorization", Authorization); 
			 }
			 
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
			 new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
			result += line;
			}
			} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
			}finally{
			try{
			if(out!=null){
			out.close();
			}
			if(in!=null){
			in.close();
			}
			}
			catch(IOException ex){
			ex.printStackTrace();
			}
			}
		
		return result;
	}
}
