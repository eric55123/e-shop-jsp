<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>管理員列表</title>
    <style>
        body {
            font-family: "微軟正黑體", sans-serif;
            background-color: #f4f4f4;
            padding: 30px;
        }

        .admin-box {
            width: 600px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 10px;
        }

        table {
            width: 100%;
        }

        td {
            padding: 8px;
            vertical-align: middle;
        }

        .btns {
            text-align: center;
            margin-top: 10px;
        }

        .btns input[type="submit"], .btns a {
            margin: 0 10px;
            padding: 6px 12px;
            font-size: 14px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
        }

        .btns a {
            background-color: #f44336;
            color: white;
            text-decoration: none;
        }

        .btns input[type="submit"] {
            background-color: #4CAF50;
            color: white;
        }

        h2 {
            text-align: center;
        }

        .section-title {
            text-align: center;
            margin-top: 40px;
            font-size: 22px;
        }

        .back-links {
            text-align: center;
            margin-top: 30px;
        }

        .back-links a {
            margin: 0 10px;
            text-decoration: none;
        }
    </style>
</head>
<body>

<h2>📋 管理員列表</h2>
<div class="back-links">
    <a href="adminCreate.action">➕ 新增管理員</a>
    <a href="/back">🔙 回後台首頁</a>
</div>

<div class="error"><s:actionerror /></div>
<div class="success"><s:actionmessage /></div>

<s:iterator value="adminList" var="admin">
    <div class="admin-box">
        <s:form action="updateAdmin" method="post">
            <table>
                <tr>
                    <td>ID：</td>
                    <td><s:property value="#admin.adminId"/></td>
                </tr>
                <tr>
                    <td>帳號：</td>
                    <td><s:textfield name="admin.username" value="%{#admin.username}" readonly="true"/></td>
                </tr>
                <tr>
                    <td>密碼：</td>
                    <td><s:password name="admin.password" placeholder="不修改請留空"/></td>
                </tr>
                <tr>
                    <td>名稱：</td>
                    <td><s:textfield name="admin.name" value="%{#admin.name}"/></td>
                </tr>
                <tr>
                    <td>信箱：</td>
                    <td><s:textfield name="admin.email" value="%{#admin.email}"/></td>
                </tr>
                <tr>
                    <td>角色：</td>
                    <td>
                        <s:select name="admin.role" value="%{#admin.role}"
                                  list="#{'super':'超管','editor':'發送者','reviewer':'審核者'}"/>
                    </td>
                </tr>
                <tr>
                    <td>狀態：</td>
                    <td>
                        <s:select name="admin.status" value="%{#admin.status}"
                                  list="#{1:'啟用', 0:'停用'}"/>
                    </td>
                </tr>
            </table>

            <div class="btns">
                <s:hidden name="admin.adminId" value="%{#admin.adminId}"/>
                <s:submit value="儲存"/>
                <a href="deleteAdmin.action?adminId=<s:property value='#admin.adminId'/>"
                   onclick="return confirm('確定要刪除嗎？')">🗑️ 刪除</a>
            </div>
        </s:form>
    </div>
</s:iterator>

</body>
</html>
