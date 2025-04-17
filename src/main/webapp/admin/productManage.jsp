<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<html>
<head>
    <title>商品管理</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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

<table border="1" cellpadding="6" cellspacing="0" id="productTable">
    <tr>
        <th>主圖</th>
        <th>編號</th>
        <th>名稱</th>
        <th>價格</th>
        <th>庫存</th>
        <th>狀態</th>
        <th>操作</th>
    </tr>
    <s:iterator value="productList" var="product">
        <tr id="row_<s:property value="#product.productNo"/>">
            <td>
                <s:if test="#product.coverImageUrl != null && #product.coverImageUrl.contains('id=')">
                    <img src="https://drive.google.com/thumbnail?id=<s:property value="#product.coverImageUrl.split('id=')[1]"/>"
                         width="60" style="border:1px solid #ccc;" />
                </s:if>
                <s:else>
                    <span style="color: gray;">無圖</span>
                </s:else>
            </td>
            <td><s:property value="#product.productNo"/></td>
            <td><s:property value="#product.productName"/></td>
            <td>$<s:property value="#product.productPrice"/></td>
            <td><s:property value="#product.remainingQty"/></td>
            <td>
                <s:if test="#product.productStatus == 1">上架</s:if>
                <s:else>下架</s:else>
            </td>
            <td>
                <a href="<s:url action='editProduct'><s:param name='productNo' value='%{#product.productNo}'/></s:url>">修改</a> |
                <a href="javascript:void(0);" onclick="deleteProduct(<s:property value='#product.productNo' />)">刪除</a>
            </td>
        </tr>
    </s:iterator>
</table>

<p><a href="/back">🔙 回後台首頁</a></p>

<script>
    function deleteProduct(productNo) {
        if (!confirm("確定要刪除這筆商品嗎？")) return;

        $.post("deleteProduct.action", { productNo: productNo })
            .done(function () {
                $("#row_" + productNo).remove();
            })
            .fail(function () {
                alert("刪除失敗，請稍後再試");
            });
    }
</script>

</body>
</html>
