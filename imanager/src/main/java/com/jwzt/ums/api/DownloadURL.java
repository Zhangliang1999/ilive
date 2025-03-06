package com.jwzt.ums.api;

import com.jwzt.common.AppTools;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadURL
{
	public static String downloadUrl(String url)
	{
		String strRet=null;
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setDoInput(true);
				
				reader=new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(),"GBK"));

				StringBuffer strBuffer=new StringBuffer(1024);
				String line=reader.readLine();
				while(line!=null)
				{
					strBuffer.append(line+"\n");
					line=reader.readLine();
				}
				strRet=strBuffer.toString();
			}catch(Exception ex)
			{
				ex.printStackTrace();
				//todo:Logger.log(ex, 1);
				strRet=null;
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}
	public static String downloadUrl(String url,String method,String encode)
	{
		String strRet=null;
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod(method);
				httpTarget.setDoInput(true);
				
				reader=new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(),encode));

				StringBuffer strBuffer=new StringBuffer(1024);
				String line=reader.readLine();
				while(line!=null)
				{
					strBuffer.append(line+"\n");
					line=reader.readLine();
				}
				strRet=strBuffer.toString();
			}catch(Exception ex)
			{
				ex.printStackTrace();
				strRet=null;
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}
	
	/**
	 * 将一段输入流传入http链接  请求中
	 * @param url
	 * @param method
	 * @param encode
	 * @param ins
	 * @return
	 */
	public static String downloadUrlWithinputStream(String url,String method,String encode,InputStream ins) {
		String strRet=null;
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("Content-Type", "text/xml");
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				OutputStream os = httpTarget.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
				reader=new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(),encode));

				StringBuffer strBuffer=new StringBuffer(1024);
				String line=reader.readLine();
				while(line!=null)
				{
					strBuffer.append(line+"\n");
					line=reader.readLine();
				}
				strRet=strBuffer.toString();
			}catch(Exception ex)
			{
				ex.printStackTrace();
				strRet=null;
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}
	/**
	 * 接收http 链接  响应的流  并转为自己响应的输出流
	 * @param url
	 * @param method
	 * @param encode
	 * @param response
	 */
	public static void downloadUrlWithoutStream(String url,String method,String encode, HttpServletResponse response) {
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("Content-Type", "text/xml");
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				InputStream ins = httpTarget.getInputStream();
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
	}
	
	
	/**
	 * 将一段输入流传入http链接  请求中
	 * @param url
	 * @param method
	 * @param encode
	 * @param ins
	 * @return
	 */
	public static Document downloadUrlWithXML(String url, String method, String encode, Document document) {
		Document strRet=null;
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("Content-Type", "text/xml");
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				OutputStream os = httpTarget.getOutputStream();
				
				//格式化输出xml文件字符串  
		        XMLOutputter xmlout = new XMLOutputter();
		        xmlout.output(document, os);
				os.close();
				
				SAXBuilder builder = new SAXBuilder();
				strRet = builder.build(httpTarget.getInputStream());

			}catch(Exception ex)
			{
				System.out.println("访问UMS报错："+url);
				ex.printStackTrace();
				strRet=null;
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}
	
	
	/**
	 * 将一段输入流传入http链接  请求中
	 * @param url
	 * @param method
	 * @param encode
	 * @param ins
	 * @return
	 */
	public static String downloadUrlStringWithXML(String url,String method,String encode,Document document) {
		String strRet=null;
		if(url!=null)
		{
			BufferedReader reader=null;
			HttpURLConnection httpTarget=null;
			try
			{
				URL target=new URL(url);
				httpTarget=(HttpURLConnection)target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("Content-Type", "text/xml");
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				OutputStream os = httpTarget.getOutputStream();
				
				//格式化输出xml文件字符串  
		        XMLOutputter xmlout = new XMLOutputter();
		        xmlout.output(document, os);

				os.close();
				
				reader=new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(),encode));

				StringBuffer strBuffer=new StringBuffer(1024);
				String line=reader.readLine();
				while(line!=null)
				{
					strBuffer.append(line+"\n");
					line=reader.readLine();
				}
				strRet=strBuffer.toString();

				
				
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
				strRet=null;
			}finally
			{
				AppTools.closeReader(reader);
				AppTools.closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}
	
}