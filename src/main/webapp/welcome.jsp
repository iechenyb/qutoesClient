<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String basePath = "http://" + request.getServerName() + ":"
		+ request.getServerPort() + request.getContextPath() + "/";
Object user = request.getSession().getAttribute("user");
Object token = request.getSession().getAttribute("token");
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
        <center>
                    欢迎你:<%=name%>。<a href="<%=basePath%>logout">安全退出</a> <a href="<%=basePath%>welcome.jsp">再次访问当前页面</a>    
        <br/>  
        <a href="<%=basePath%>order.jsp">查看订单</a>&nbsp;&nbsp;<a href="<%=basePath%>user.jsp">查看用户信息</a>
         <br/> 
         token=<%=token%> 
        </center> 
  </body>  
</html>  