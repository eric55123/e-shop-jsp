<%@ page import="com.eshop.admin.model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>常見問題管理</title>
    <style>
        body {
            font-family: sans-serif;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
        }
        th {
            background-color: #f4f4f4;
        }
        h3 {
            margin-top: 40px;
        }
        form {
            margin-top: 30px;
            padding: 15px;
            border: 1px solid #aaa;
            background: #f9f9f9;
            width: 700px;
        }
    </style>
</head>
<body>

<%
    Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
    if (loggedInAdmin == null) {
        response.sendRedirect(request.getContextPath() + "/adminLogin.action");
        return;
    }

%>

<p style="text-align: right;">
    👤 歡迎，<strong><%= loggedInAdmin.getName() %></strong>（帳號：<%= loggedInAdmin.getUsername() %>） |
    <a href="adminLogout.action">🚪 登出</a>
    <a href="/back">🔙 回後台首頁</a>
</p>

<h2>📋 常見問題後台管理</h2>

<!-- ✅ FAQ 清單 -->
<table>
    <tr>
        <th>排序</th>
        <th>問題</th>
        <th>回答</th>
        <th>分類</th>
        <th>狀態</th>
        <th>操作</th>
    </tr>
    <s:iterator value="faqList" var="faq">
        <tr>
            <td><s:property value="#faq.sortOrder" /></td>
            <td><s:property value="#faq.question" /></td>
            <td><s:property value="#faq.answer" /></td>
            <td><s:property value="#faq.category" /></td>
            <td>
                <s:if test="#faq.isEnabled == 1">顯示</s:if>
                <s:else>隱藏</s:else>
            </td>
            <td>
                <a href="faqEdit.action?faqId=<s:property value='#faq.faqId'/>">✏️ 編輯</a> |
                <a href="faqDelete.action?faqId=<s:property value='#faq.faqId'/>"
                   onclick="return confirm('確定要刪除這筆常見問題嗎？')">🗑️ 刪除</a>
            </td>
        </tr>
    </s:iterator>
</table>

<!-- ✅ FAQ 表單：新增 / 編輯 -->
<h3><s:if test="faq != null">✏️ 編輯常見問題</s:if><s:else>➕ 新增常見問題</s:else></h3>

<s:form action="%{faq != null ? 'faqUpdate' : 'faqAddSubmit'}" method="post">
    <s:hidden name="faq.faqId"/>

    <table style="width: 100%; max-width: 700px;">
        <tr>
            <td style="width: 100px;">問題：</td>
            <td><s:textfield name="faq.question" size="50" required="true"/></td>
        </tr>
        <tr>
            <td>回答：</td>
            <td><s:textarea name="faq.answer" cols="50" rows="4" required="true"/></td>
        </tr>
        <tr>
            <td>分類：</td>
            <td><s:textfield name="faq.category" size="30"/></td>
        </tr>
        <tr>
            <td>排序：</td>
            <td><s:textfield name="faq.sortOrder" size="5"/></td>
        </tr>
        <tr>
            <td>是否啟用：</td>
            <td><s:radio name="faq.isEnabled" list="#{'1':'啟用','0':'隱藏'}"/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:center;">
                <s:submit value="%{faq != null ? '更新' : '新增'}"/>
                <s:reset value="清除" style="margin-left: 10px;"/>
            </td>
        </tr>
    </table>
</s:form>

</body>
</html>
