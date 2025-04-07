<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.cartitem.model.CartItem" %>

<%
    Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

    String referer = request.getHeader("referer");
    String redirected = request.getParameter("redirected");

    if (referer != null && referer.contains("addToCart.action") && redirected == null) {
        response.sendRedirect("cartitem/cart.jsp?redirected=true");
        return;
    }
%>



<html>
<head>
    <title>購物車</title>
    <script>
        function doubleConfirmClear() {
            if (confirm('確定要清空購物車嗎？')) {
                return confirm('此操作將移除所有商品，確定繼續？');
            }
            return false;
        }
    </script>
</head>
<body>
<h2>購物車</h2>

<% if (cart == null || cart.isEmpty()) { %>
<p>目前購物車是空的。</p>
<% } else { %>
<table border="1">
    <tr>
        <th>商品名稱</th>
        <th>單價</th>
        <th>數量</th>
        <th>小計</th>
    </tr>
    <% BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.values()) {
            Product p = item.getProduct();
            int qty = item.getQuantity();
            BigDecimal subtotal = p.getProductPrice().multiply(new BigDecimal(qty));
            total = total.add(subtotal);
    %>
    <tr>
        <td><%= p.getProductName() %></td>
        <td>$<%= p.getProductPrice() %></td>
        <td>
            <form action="updateCart.action" method="post" style="display:inline;">
                <input type="hidden" name="productNo" value="<%= p.getProductNo() %>">
                <input type="number" name="quantity" value="<%= qty %>" min="0" max="<%= p.getRemainingQty() %>">
                <button type="submit">更新</button>
            </form>
            <form action="removeFromCart.action" method="post" style="display:inline;">
                <input type="hidden" name="productNo" value="<%= p.getProductNo() %>">
                <button type="submit" onclick="return confirm('確定要移除這個商品嗎？')">移除</button>
            </form>
        </td>
        <td>$<%= subtotal %></td>
    </tr>
    <% } %>
    <tr>
        <td colspan="3" align="right"><strong>總計：</strong></td>
        <td>$<%= total %></td>
    </tr>
</table>
<br>
<form action="checkout.action" method="post">
    <button type="submit">我要結帳</button>
</form>
<form action="clearCart.action" method="post">
    <button type="submit" onclick="return confirm('確定要清空購物車嗎？')">清空購物車</button>
</form>

<% } %>

<br>
<a href="productList.action">← 回商品列表</a>
</body>
</html>