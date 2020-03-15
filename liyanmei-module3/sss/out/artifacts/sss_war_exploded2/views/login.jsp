<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
        <form action="/login/login.action" method="post" align="center">
            <h2>登录</h2>
            <table align="center">
                <tr><td align="right">用户名：</td><td><input type="text" name="username"></td></tr>
                <tr><td align="right">密码：</td><td><input type="password" name="password"></td></tr>
                <tr><td colspan="2" align="center"><input type="submit" value="登陆"></td></tr>

            </table>
        </form>
</body>
</html>
