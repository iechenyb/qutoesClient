<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String basePath = "http://" + request.getServerName() + ":"
		+ request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>  
  <head>  
  </head>  
  <body>  
        <p>在同一台服务器上，多个站点自动登录....>>:<%=session.getId()%></p>  
            <form name="f" method="post" action="<%=basePath%>login">  
                Name：<input type="text" name="name" value='it1'/><br/>  
                Pwd：<input type="text" name="pwd" value='pwd2'/><br/>  
                <input type="checkbox" name="chk" value="7">一周内自动登录<br/>  
                <input type="submit" value="登录"/>           
            </form>  
        ${not empty sessionScope.user}
                    欢迎你:${user}。<a href="<%=basePath%>logout">安全退出</a>  
        <br/>  
        相关站点：（只要在一边登录成功，即可以自动登录到另一个程序）<br/>  
        <a href="http://mail.itcast.com:7777">mail.itcast.com</a><br/>  
        <a href="http://bbs.itcast.com:7777">bbs.itcast.com</a><br/>  
  </body>  
</html>  