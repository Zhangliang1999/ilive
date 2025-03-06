package com.jwzt.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwzt.jssdk.JSSDKInfo;
import com.jwzt.jssdk.JSSDKMgr;

public class JSSDKServlet extends HttpServlet {

	private static final long serialVersionUID = -227688634354179879L;

	
	 @Override
	 public void init(ServletConfig arg0) throws ServletException {
		 String appId = arg0.getInitParameter("appId");
		 String appSecret = arg0.getInitParameter("appSecret");
		 JSSDKMgr.appId = appId;
		 JSSDKMgr.appSecret = appSecret;
		 
		 
	 }

	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String url = request.getParameter("shareUrl");
		try {
			JSSDKInfo info = JSSDKMgr.setShareConfig(url);
			org.json.JSONObject jsonObj = new org.json.JSONObject(info);
			String retstr = jsonObj.toString();
	        retstr  = retstr.replaceAll("\\\\n", "");
	        String callback = request.getParameter("callback");
	        if(callback != null){
				out.print(callback);
				out.print('(');
			}
	        out.println(retstr);
	        if(callback != null){
				out.print(')');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
