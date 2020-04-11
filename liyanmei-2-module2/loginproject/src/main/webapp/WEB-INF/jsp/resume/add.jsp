<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>add</title>
</head>
<body>
<h2>我是服务器：${pageContext.request.localPort}</h2>
<h2>当前sessionId：${pageContext.session.id}</h2>
<form action="/resume/save" method="post" align="center">
    <h1>新增 Resume 信息</h1>
    <table align="center">
        <tr><td align="right">姓名：</td><td><input type="text" name="name"></td></tr>
        <tr><td align="right">地址：</td><td><input type="text" name="address"></td></tr>
        <tr><td align="right">电话：</td><td><input type="text" name="phone"></td></tr>
        <tr><td colspan="2" align="center"><input type="submit" value="提交"></td></tr>

    </table>
</form>
</body>
</html>
