package com.bvRadio.iLive.iLive.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {

	public static String down(String savePath, String name, String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String path = savePath + "/" + name;
			File file = new File(path);
			if (!file.exists()) {
				// 不存在
				request.setAttribute("name", fileName);
				return "download_error";// 返回下载文件不存在
			}
			response.setContentType("application/octet-stream");
			// 根据不同浏览器 设置response的Header
			String userAgent = request.getHeader("User-Agent").toLowerCase();
			if (userAgent.indexOf("msie") != -1) {
				// ie浏览器
				// System.out.println("ie浏览器");
				response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
			} else {
				response.addHeader("Content-Disposition",
						"attachment;filename=" + new String(name.getBytes("utf-8"), "ISO8859-1"));
			}
			response.addHeader("Content-Length", "" + file.length());
			// 以流的形式下载文件
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// response.setContentType("image/*"); // 设置返回的文件类型
			OutputStream toClient = response.getOutputStream();
			OutputStream bos = new BufferedOutputStream(toClient);
			// BufferedWriter bw = new BufferedWriter(new
			// OutputStreamWriter(bos));
			bos.write(buffer);
			// bw.close();
			bos.close();
			toClient.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			// response.reset();
			return "exception";// 返回异常页面
		} finally {
			/*
			 * if (toClient != null) { try { toClient.close(); } catch
			 * (IOException e) { e.printStackTrace(); } }
			 */
		}
	}

}
