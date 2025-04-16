<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<html>
<head>
    <title>商品管理</title>
</head>
<body>

<%
    Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
    if (loggedInAdmin == null) {
        response.sendRedirect(request.getContextPath() + "/adminLogin.action");
        return;
    }
%>

<div style="text-align:right;">
    👤 歡迎，<strong><%= loggedInAdmin.getName() %></strong>（帳號：<%= loggedInAdmin.getUsername() %>）
    | <a href="adminLogout.action">登出</a>
</div>

<h2>商品管理</h2>
<p>
    <a href="showAddProduct.action">➕ 新增商品</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>編號</th>
        <th>名稱</th>
        <th>價格</th>
        <th>庫存</th>
        <th>狀態</th>
        <th>操作</th>
    </tr>
    <s:iterator value="productList">
        <tr>
            <td><s:property value="productNo"/></td>
            <td><s:property value="productName"/></td>
            <td>$<s:property value="productPrice"/></td>
            <td><s:property value="remainingQty"/></td>
            <td>
                <s:if test="productStatus == 1">上架</s:if>
                <s:else>下架</s:else>
            </td>
            <td>
                <a href="<s:url action='editProduct'>
                    <s:param name='productNo' value='productNo'/>
                </s:url>">修改</a> |

                <a href="<s:url action='deleteProduct'>
                    <s:param name='productNo' value='productNo'/>
                </s:url>"
                   onclick="return confirm('確定要刪除這筆商品嗎？')">刪除</a>
            </td>
        </tr>
    </s:iterator>
</table>

<p><a href="/back">🔙 回後台首頁</a></p>

</body>
</html>
