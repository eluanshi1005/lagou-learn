<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>add</title>
</head>
<body>
<form action="/resume/save.action" method="post" align="center">
    <h2>修改 Resume 信息</h2>
    <table align="center">
        <input type="text" name="id" value="${resume.id}" hidden>
        <tr><td align="right">姓名：</td><td><input type="text" name="name" value="${resume.name}"></td></tr>
        <tr><td align="right">地址：</td><td><input type="text" name="address" value="${resume.address }"></td></tr>
        <tr><td align="right">电话：</td><td><input type="text" name="phone" value="${resume.phone }"></td></tr>
        <tr><td colspan="2" align="center"><input type="submit" value="提交"></td></tr>

    </table>
</form>
</body>
</html>
