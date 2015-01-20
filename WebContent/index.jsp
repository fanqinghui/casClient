<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>index.jsp</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>

	<body>
		客户端登陆页登陆测试！---
		<hr>
		当前session中用户是：<font color="red"><%=session.getAttribute("currentUserName")%></font>
		<hr>
		ticket:<font color="red"><%=request.getParameter("ticket")%></font>
		<br>
		<a href="http://10.2.70.118:8080/cas/logout">log out</a>
	</body>
</html>
