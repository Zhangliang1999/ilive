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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<table>
			<thead>
				<tr bgcolor="gray">
					<td width="200" >
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
			<c:forEach items="${Disklist.fileList}" var="file">
				<tbody>
					<tr>
						<td>
						<img src="<%=basePath%>${file.file_img}" width="15px" height="15px">
						<a href="<%=basePath%>servlet/PathToFileServlet?path=${file.file_path}">${file.file_name}</a>
						</td>
						<td>
							${file.file_updateTime}
						</td>
						<td>
							${file.file_type}
						</td>
						<c:if test="${file.file_size!=null}">
						<td>
						${file.file_size}KB
						</td>
						</c:if>
						<c:if test="${file.file_size==null}">
						<td>
						</td>
						</c:if>
						<td>
						<script type="text/javascript">
						function del(num1,num2){
							if(confirm("确定删除该记录？")){
								window.location="<%=basePath%>servlet/PathToDelete?deletepath="+num1+"&parent="+num2;
							}
						}
						</script>
						<a onclick='del("${file.file_path}","${parent}")'>删除</a>&nbsp&nbsp
						<c:if test="${file.file_mp4==1&&file.file_qiepian==0}">
						<a href="<%=basePath%>servlet/CanM3U8Servlet?canpath=${file.file_path}&parent=${parent}">切片</a>
						</c:if>
						<c:if test="${file.file_qiepian==1}">
						已切片
						</c:if>
						</td>
					</tr>
				</tbody>
			</c:forEach>
		</table>

	</body>
</html>