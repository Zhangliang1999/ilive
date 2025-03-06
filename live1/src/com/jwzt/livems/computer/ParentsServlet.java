package com.jwzt.livems.computer;

/**
 * 
 * 获取数据库路径，显示路径
 */
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParentsServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("contentType", "text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		List<String> findpDiskBeans = ComputerFind.findpDiskBeans();
		request.setAttribute("parent", findpDiskBeans);
		request.getRequestDispatcher("../ParentPath.jsp").forward(request,
				response);
	}
}
