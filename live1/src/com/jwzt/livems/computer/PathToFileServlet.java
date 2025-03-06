package com.jwzt.livems.computer;

/**
 * 
 * 根据页面传过来的路径，获取路径下面的所有文件
 */
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathToFileServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("contentType", "text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		DiskBean parentsToFile = null;
		String parameter = request.getParameter("path");
		try {
			File f = new File(parameter);
			// 判断是否为文件
			if (f.isFile()) {
				System.out.println("123");
				// parentsToFile = ComputerFind.ParentsToFile(parameter);
				// request.setAttribute("Disklist",parentsToFile);
				// request.setAttribute("parent", parameter);
				// request.getRequestDispatcher("../DiskList.jsp").forward(request,
				// response);
			} else {
				parentsToFile = ComputerFind.ParentsToFile(parameter);
				request.setAttribute("Disklist", parentsToFile);
				request.setAttribute("parent", parameter);
				request.getRequestDispatcher("../DiskList.jsp").forward(
						request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
