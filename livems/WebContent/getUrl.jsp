<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.jwzt.DB.soms.vod.image.file.ImgPublicMgr" %>

<%
request.setCharacterEncoding("GBK");
String newsId = request.getParameter("newsId");
String siteId = request.getParameter("siteId");
ImgPublicMgr imgMgr = new ImgPublicMgr();
List imgList=imgMgr.getFileList(" from ImageFileInfo where file_id=34395");
%>