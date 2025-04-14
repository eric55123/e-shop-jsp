<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<%
    Admin loginAdmin = (Admin) session.getAttribute("loggedInAdmin");
    boolean isSuper = loginAdmin != null && "super".equalsIgnoreCase(loginAdmin.getRole());
%>

<html>
<head>
    <title>新增管理員與列表管理</title>
    <style>
        body {
            font-family: "微軟正黑體", sans-serif;
            background-color: #f4f4f4;
            padding: 30px;
        }

        .form-box {
            width: 800px;
            margin: auto;
            padding: 30px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
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

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
        }

        th, td {
            border: 1px solid #aaa;
            padding: 8px;
            text-align: center;
        }

        .btn-back {
            margin-top: 20px;
            text-align: center;
        }

        .btn-back button {
            padding: 8px 16px;
            font-size: 16px;
            border-radius: 5px;
            border: none;
            background-color: #ccc;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="form-box">

    <% if (!isSuper) { %>
    <!-- 非 super 的人看到這段 -->
    <p class="error">⚠ 您沒有權限進入此頁面</p>

    <div class="btn-back">
        <button onclick="history.back()">⬅ 返回上一頁</button>
    </div>
    <% } else { %>
    <!-- super 可見內容 -->
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

    <hr>
    <h2>📋 目前管理員列表</h2>

    <table>
        <tr>
            <th>ID</th><th>帳號</th><th>名稱</th><th>信箱</th><th>角色</th><th>狀態</th><th>操作</th>
        </tr>

        <s:iterator value="adminList" var="admin">
            <s:form action="updateAdmin" method="post">
                <tr>
                    <td><s:property value="#admin.adminId"/></td>
                    <td><s:textfield name="admin.username" value="%{#admin.username}" readonly="true"/></td>
                    <td><s:textfield name="admin.name" value="%{#admin.name}"/></td>
                    <td><s:textfield name="admin.email" value="%{#admin.email}"/></td>
                    <td>
                        <s:select name="admin.role" value="%{#admin.role}"
                                  list="#{'super':'超管','editor':'發送者','reviewer':'審核者'}"/>
                    </td>
                    <td>
                        <s:select name="admin.status" value="%{#admin.status}"
                                  list="#{1:'啟用', 0:'停用'}"/>
                    </td>
                    <td>
                        <s:hidden name="admin.adminId" value="%{#admin.adminId}"/>
                        <s:submit value="儲存"/>
                        <a href="deleteAdmin.action?adminId=<s:property value='#admin.adminId'/>"
                           onclick="return confirm('確定要刪除嗎？')">刪除</a>
                    </td>
                </tr>
            </s:form>
        </s:iterator>
    </table>
    <% } %>

</div>
</body>
</html>
