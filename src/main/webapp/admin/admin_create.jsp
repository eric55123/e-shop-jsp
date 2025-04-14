<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增管理員</title>
    <style>
        body {
            font-family: "微軟正黑體", sans-serif;
            background-color: #f7f7f7;
            padding: 30px;
        }

        h2 {
            text-align: center;
        }

        .error {
            color: red;
            text-align: center;
            font-weight: bold;
        }

        .success {
            color: green;
            text-align: center;
            font-weight: bold;
        }

        .btn-back {
            text-align: center;
            margin-top: 20px;
        }

        .btn-back a {
            margin: 0 10px;
            text-decoration: none;
            color: #333;
        }
    </style>
</head>
<body>

<h2>👑 新增管理員</h2>

<div class="error"><s:actionerror /></div>
<div class="success"><s:actionmessage /></div>

<s:form action="adminCreate" method="post">
    <s:textfield name="newAdmin.username" label="帳號" required="true"/>
    <s:password name="newAdmin.password" label="密碼" required="true"/>
    <s:textfield name="newAdmin.name" label="名稱" required="true"/>
    <s:textfield name="newAdmin.email" label="信箱"/>
    <s:select name="newAdmin.role" label="角色"
              list="#{'editor':'發送者','reviewer':'審核者'}" required="true"/>
    <s:radio name="newAdmin.status" label="啟用狀態"
             list="#{1:'啟用', 0:'停用'}" required="true"/>
    <s:submit value="新增管理員"/>
</s:form>

<div class="btn-back">
    <a href="adminManage.action">📋 前往管理員列表</a>
    <a href="/back">🔙 回後台首頁</a>
</div>

</body>
</html>
