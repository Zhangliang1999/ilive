<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<table>
			<thead>
				<tr bgcolor="gray">
					<td width="200">
						文件名字
					</td>
					<td width="100">
						修改日期
					</td>
					<td width="150">
						文件类型
					</td>
					<td width="100">
						文件大小
					</td>
					<td width="150">
						文件操作
					</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${parent}" var="p">
					<tr>
						<td>
							<a href="<%=basePath%>servlet/PathToFileServlet?path=${p}">${p}</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>
