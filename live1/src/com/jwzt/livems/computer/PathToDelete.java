package com.jwzt.livems.computer;

/**
 * 对文件进行删除，可删除文件和文件夹
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathToDelete extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("contentType", "text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String parameter = request.getParameter("deletepath");
		String parent = request.getParameter("parent");
		System.out.println("ok!!!!" + parent);
		boolean deletefile = ComputerFind.deletefile(parameter);
		DiskBean parentsToFile = null;
		try {
			// 删除后返回上一层路径，获取所有文件
			parentsToFile = ComputerFind.ParentsToFile(parent);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (deletefile) {
			request.setAttribute("Disklist", parentsToFile);
			request.getRequestDispatcher("../DiskList.jsp").forward(request,
					response);
		} else {
			request.setAttribute("Disklist", parentsToFile);
			request.setAttribute("status", "删除失败");
			request.getRequestDispatcher("../DiskList.jsp").forward(request,
					response);
		}
	}
}
