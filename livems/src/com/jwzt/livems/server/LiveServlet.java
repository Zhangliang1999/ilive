package com.jwzt.livems.server;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwzt.common.Logger;
import com.jwzt.livems.listener.FileProgramM3U8Task;
import com.jwzt.livems.live.RecordLiveMgr;

/**
 * 收录通知
 * 
 * @author administrator
 */
public class LiveServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		service(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		service(request, response);
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=UTf-8");
		String function = request.getParameter("function");
		StringBuffer log = new StringBuffer();
		log.append("###########################################").append("\r\n");
		log.append("LiveServlet").append("\r\n");
		log.append("function:" + function).append("\r\n");
		log.append("mountName:" + request.getParameter("mountName")).append("\r\n");
		log.append("###########################################").append("\r\n");
		System.out.print(log.toString());
		Logger.log(log.toString(), 3);

		/**
		 * 直播收录
		 */
		if ("RecordLive".equals(function)) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String mountName = request.getParameter("mountName");
				String startTime = request.getParameter("startTime");
				String length = request.getParameter("length");
				String path = request.getParameter("path");
				String ftpPath = request.getParameter("ftpPath");
				if (ftpPath == null)
					ftpPath = "";

				// 源服务器ID
				String serverGroupId = request.getParameter("serverGroupId");

				// 目标服务器ID
				String destGroupId = request.getParameter("destGroupId");

				log = new StringBuffer();
				log.append("###########################################").append("\r\n");
				log.append("startTime:" + startTime).append("\r\n");
				log.append("length:" + length).append("\r\n");
				log.append("###########################################").append("\r\n");
				System.out.print(log.toString());
				Logger.log(log.toString(), 3);
				Date date = formatter.parse(startTime);
				Timestamp tStartTime = new Timestamp(date.getTime());
				RecordLiveMgr mgr = new RecordLiveMgr();

				// 开始录制
				String ret = mgr.recordLive(mountName, tStartTime, Integer.parseInt(length), path, serverGroupId,
						destGroupId, ftpPath);

				// 录制结果
				if (ret != null && ret.length() > 0) {
					ret = ret.substring(0, ret.length() - 1);
				}

				ret = "<ret>" + ret + "</ret>";
				response.getWriter().write(ret);
				response.getWriter().close();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if ("SegmentLive".equals(function)) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String mountName = request.getParameter("mountName");
				String path = request.getParameter("path");
				String m3u8_beginmessage = request.getParameter("m3u8_beginmessage");
				String m3u8_endmessage = request.getParameter("m3u8_endmessage");
				String serverGroupId = request.getParameter("serverGroupId");
				String destGroupId = request.getParameter("destGroupId");
				String ftppath = request.getParameter("ftppath");
				log = new StringBuffer();
				log.append("###########################################").append("\r\n");
				log.append("m3u8_beginmessage:" + m3u8_beginmessage).append("\r\n");
				log.append("m3u8_endmessage:" + m3u8_endmessage).append("\r\n");
				log.append("###########################################").append("\r\n");
				System.out.print(log.toString());
				Logger.log(log.toString(), 3);
				RecordLiveMgr mgr = new RecordLiveMgr();
				String ret = mgr.segmentLive(mountName, m3u8_beginmessage, m3u8_endmessage, path, serverGroupId,
						destGroupId, ftppath);
				if (ret != null && ret.length() > 0) {
					ret = ret.substring(0, ret.length() - 1);
				}
				ret = "<ret>" + ret + "</ret>";
				response.getWriter().write(ret);
				response.getWriter().close();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ("FileProgram".equals(function)) {
			try {
				FileProgramM3U8Task.channelList = null;
				String channelId = request.getParameter("channelId");
				String ymd = request.getParameter("ymd");
				RecordLiveMgr mgr = new RecordLiveMgr();
				String ret = mgr.FileProgram(Integer.parseInt(channelId), ymd);
				if (ret != null && ret.length() > 0) {
					ret = ret.substring(0, ret.length() - 1);
				}
				ret = "<ret>" + ret + "</ret>";
				response.getWriter().write(ret);
				response.getWriter().close();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
