<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>resume</title>

    <style>
        div{
            padding:10px 10px 0 10px;
        }
    </style>
</head>
<body>
<div align="center">

    <table border="1"  cellspacing="0">
        <tr><td colspan="5" align="right"><input type="button" id="add" value="新增" onclick="location.href='/resume/add.action'"/></td></tr>
        <tr>
            <td>编号</td>
            <td>姓名</td>
            <td>地址</td>
            <td>电话</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${list}" var="resume">
            <tr>
                <td>${resume.id }</td>
                <td>${resume.name }</td>
                <td>${resume.address }</td>
                <td>${resume.phone }</td>
                <td>
                    <a href="/resume/update.action?id=${resume.id }">修改</a>
                    <a href="/resume/delete.action?id=${resume.id }">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
