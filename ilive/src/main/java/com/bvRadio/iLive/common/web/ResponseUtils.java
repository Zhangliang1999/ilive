package com.bvRadio.iLive.common.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
	public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 * 
	 * @param isflush
	 *            是否刷新并关闭
	 */
	public static void renderJson(HttpServletResponse response, String text, boolean isflush) {
		render(response, "application/json;charset=UTF-8", text, isflush);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderHtml(HttpServletResponse response, String text) {
		render(response, "text/html;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType, String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType, String text, boolean isflush) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
			if (isflush) {
				response.getWriter().flush();
				response.getWriter().close();
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void renderJson(HttpServletRequest request, HttpServletResponse response, String text) {
		if (request.getParameter("callback") != null && !request.getParameter("callback").trim().equals("")) {
			render(response, "application/json;charset=UTF-8", request.getParameter("callback") + "(" + text + ")");
		} else {
			render(response, "application/json;charset=UTF-8", text);
		}
	}
	public static void renderJsonNoJsonp(HttpServletRequest request, HttpServletResponse response, String text) {
		
			render(response, "application/json;charset=UTF-8", text);
		
	}
	public static void renderJsonCros(HttpServletRequest request, HttpServletResponse response, String text) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (request.getParameter("callback") != null && !request.getParameter("callback").trim().equals("")) {
			render(response, "application/json;charset=UTF-8", request.getParameter("callback") + "(" + text + ")");
		} else {
			render(response, "application/json;charset=UTF-8", text);
		}
	}
	
	/**
	 * 下载action统一处理
	 * 
	 * @param fileName
	 * @param os
	 * @param response
	 */
	public static void downloadHandler(String fileName, ByteArrayOutputStream os, HttpServletResponse response) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
