package com.bvRadio.iLive.iLive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bvRadio.iLive.iLive.entity.ParamsBean;

public class HttpUtils2 {

	public static String sendStr(String requestUrl, String requestMethod, String outputStr) {
		String result = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("content-type", "text/html");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.flush();
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			result = buffer.toString();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String doDelete(String data, String url) throws IOException {
		CloseableHttpClient client = null;
		HttpDeleteWithBody httpDelete = null;
		String result = null;
		try {
			client = HttpClients.createDefault();
			httpDelete = new HttpDeleteWithBody(url);
			 
			httpDelete.addHeader("Content-type","application/json; charset=utf-8");
			httpDelete.setHeader("Accept", "application/json; charset=utf-8");
			httpDelete.setEntity(new StringEntity(data));
 
			CloseableHttpResponse response = client.execute(httpDelete);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
 
			
		} catch (Exception e) {
			
		} finally {
			client.close();
		}
		return result;
 
	}

	/**
	 * 使用Get方式获取数据
	 * 
	 * @param url
	 *            URL包括参数，http://HOST/**?**=**&***=***
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String sendGet(String url, String charset) throws IOException {
		String result = "";
		BufferedReader in = null;
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection connection = realUrl.openConnection();
		// 设置通用的请求属性
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 建立实际的连接
		connection.connect();
		// 定义 BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		// 使用finally块来关闭输入流
		if (null != in) {
			in.close();
		}
		return result;
	}

	/**
	 * POST请求，字符串形式数据
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 */
	public static String sendPost(String url, String param, String charset) throws IOException {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		// 使用finally块来关闭输出流、输入流
		if (out != null) {
			out.close();
		}
		if (in != null) {
			in.close();
		}
		return result;
	}

	/**
	 * POST请求，Map形式数据
	 * 
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 */
	public static String sendPost(String url, Map<String, String> params, String charset) throws IOException {

		StringBuffer buffer = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(buffer);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		// 使用finally块来关闭输出流、输入流
		if (out != null) {
			out.close();
		}
		if (in != null) {
			in.close();
		}
		return result;
	}
	public static String httpSend(String url, List<ParamsBean> params,
			RequestCallback rcb) {
		try {
			URL mUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDefaultUseCaches(false);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter out = new PrintWriter(connection.getOutputStream());
			// 发送请求参数
			out.print(getValue(params));
			// flush输出流的缓冲
			out.flush();
			InputStream is = connection.getInputStream();
			String result = StreamUtils.getStringFromStream(is);
			rcb.callBack(result);
			is.close();
			out.close();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("链接失败");
		}
		return null;

	}
	private static String getValue(List<ParamsBean> params) {
		String value = "";
		for (int i = 0; i < params.size(); i++) {
			ParamsBean bean = params.get(i);
			value += bean.getKey() + "=" + bean.getValues();
			if (i + 1 != params.size()) {
				value += "&";
			}
		}

		return value;
	}

	
	
}
