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
    <div class="heading">欢迎加入Jchat</div>
    <form action="" class="form">
      <input required class="input" type="text" name="uname" id="uname" placeholder="用户名">
      <input required class="input" type="password" name="password" id="password" placeholder="密码">
      <input required class="input" type="password" name="password2" id="password2" placeholder="确认密码">
      <input required class="input" type="email" name="email" id="email" placeholder="邮箱">
      <input class="login-button" type="submit" value="注册">
    </form>  
  </div>
</body>
</html>