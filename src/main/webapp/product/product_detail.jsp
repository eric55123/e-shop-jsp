<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="java.util.*" %>

<%
    Product product = (Product) request.getAttribute("product");
%>
<html>
<head>
    <title>商品詳情</title>
</head>
<body>
<h2>商品詳情</h2>

<% if (product != null) { %>
<p><strong>商品名稱：</strong> <%= product.getProductName() %></p>
<p><strong>價格：</strong> $<%= product.getProductPrice() %></p>
<p><strong>描述：</strong> <%= product.getProductDesc() != null ? product.getProductDesc() : "無" %></p>
<p><strong>剩餘庫存：</strong> <%= product.getRemainingQty() %></p>
<p><strong>狀態：</strong> <%= product.getProductStatus() == 1 ? "上架中" : "已下架" %></p>

<form action="addToCart.action" method="post">
    <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
    <button type="submit">加入購物車</button>
</form>
<% } else { %>
<p>查無此商品。</p>
<% } %>

<br>
<a href="productList.action">回商品列表</a>
</body>
</html>
