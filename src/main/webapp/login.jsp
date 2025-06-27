<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" href="static/css/login.css">
</head>
<body>
  <div class="container">
    <div class="heading">Jchat</div>
    <form action="login" method="post" class="form">
      <input required class="input" type="text" name="uname" placeholder="用户名">
      <input required class="input" type="password" name="password" placeholder="密码">
      <span class="forgot-password"><a href="register.jsp">立即注册</a></span>
      <input class="login-button" type="submit" value="登录">
      <% if(request.getAttribute("error") != null) { %>
      <div class="error-message" style="color: #f45a5a; margin-top: 10px">
        <%= request.getAttribute("error") %>
      </div>
      <% } %>
    </form>  
  </div>
</body>
</html>