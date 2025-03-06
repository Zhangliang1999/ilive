package com.jwzt.livems.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.input.SAXBuilder;

import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.DB.soms.vod.file.FileMgr;
import com.jwzt.common.Logger;
import com.jwzt.livems.live.RecordLiveMgr;
import com.jwzt.livems.live.RecordLiveVodMgr;
import com.jwzt.livems.vod.RecordVodMgr;

public class VodForLiveServlet  extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		service(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		service(request, response);
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		RecordLiveVodMgr mgr = new RecordLiveVodMgr();
		RecordVodMgr vodMgr = new RecordVodMgr();
		FileMgr fileMgr = new FileMgr();
		response.setContentType("text/html; charset=UTf-8");
		String function = request.getParameter("function");
		//System.out.println("function==="+function);
		
		StringBuffer log = new StringBuffer();
		log.append("###########################################").append("\r\n");
		log.append("VodForLiveServlet").append("\r\n");
		log.append("function:"+function).append("\r\n");
		log.append("mountName:"+request.getParameter("mountName")).append("\r\n");
		log.append("startTime:"+request.getParameter("startTime")).append("\r\n");
		log.append("length:"+request.getParameter("length")).append("\r\n");
		log.append("###########################################").append("\r\n");
		System.out.print(log.toString());
		Logger.log(log.toString(), 3);
		
		if("RecordVodForLive".equals(function)){
			try{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String mountName = request.getParameter("mountName");
				//System.out.println("mountName==="+mountName);
				String startTime = request.getParameter("startTime");
				String length = request.getParameter("length");
				Date date = formatter.parse(startTime);
				Timestamp tStartTime = new Timestamp(date.getTime());
				
				String ret = mgr.recordLive(mountName, tStartTime , Integer.parseInt(length)  );
				if(ret != null && ret.length()>0){
					ret = ret.substring(0,ret.length()-1);
				}
				ret = "<ret>"+ret+"</ret>";
				response.getWriter().write(ret);
				response.getWriter().close();
				return;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}else if("cutPIC".equals(function)){
			String cutTime = request.getParameter("cutTime");
			String fileId = request.getParameter("fileId");
			FileInfo fileInfo = fileMgr.getFileInfo(Integer.parseInt(fileId));
			String picPath = vodMgr.cutFileSinglePIC(fileInfo,cutTime);
			response.getWriter().write(picPath);
			response.getWriter().close();
		}
	}
}
