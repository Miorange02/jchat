<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>500 - 服务器内部错误</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .error-container { max-width: 800px; margin: 0 auto; }
    .error-message { background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; }
    .error-details { margin-top: 20px; }
    .stack-trace { font-family: monospace; white-space: pre-wrap; background-color: #f5f5f5; padding: 10px; overflow-x: auto; }
  </style>
</head>
<body>
<div class="error-container">
  <h1>500 - 服务器内部错误</h1>

  <div class="error-message">
    <p>服务器遇到问题，请稍后再试。</p>
    <p>错误信息: ${exception.message}</p>
  </div>

  <div class="error-details">
    <h3>错误详情 (仅供开发人员参考)</h3>
    <div class="stack-trace">
      <c:forEach items="${exception.stackTrace}" var="element">
        ${element}<br>
      </c:forEach>
    </div>
  </div>

  <a href="${pageContext.request.contextPath}/">返回首页</a>
</div>
</body>
</html>