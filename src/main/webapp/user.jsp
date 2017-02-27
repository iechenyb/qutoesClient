<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String basePath = "http://" + request.getServerName() + ":"
		+ request.getServerPort() + request.getContextPath() + "/";
Object user = request.getSession().getAttribute("user");
String name = "";
if(user!=null){
	name = user.toString();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>  
  <head>  
  </head>  
  <body>                      
     <center>查看用户的信息</center>
  </body>  
</html>  