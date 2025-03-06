package com.jwzt.livems.computer;

/**
 * 对视频文件进行切片
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CanM3U8Servlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("contentType", "text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String parameter = request.getParameter("canpath");
		String parent = request.getParameter("parent");
		System.out.println("path" + parameter);
		// 根据路径执行切片，返回值为ture表示执行切片
		boolean qiepian = ComputerFind.qiepian(parameter);
		DiskBean parentsToFile = null;
		try {
			// 切片返回上一层路径，获取路径文件下所有文件
			parentsToFile = ComputerFind.ParentsToFile(parent);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 成功
		if (qiepian) {
			request.setAttribute("Disklist", parentsToFile);
			request.getRequestDispatcher("../DiskList.jsp").forward(request,
					response);
		} else {
			request.setAttribute("Disklist", parentsToFile);
			request.setAttribute("status", "切片失败");
			request.getRequestDispatcher("../DiskList.jsp").forward(request,
					response);
		}
	}
}
