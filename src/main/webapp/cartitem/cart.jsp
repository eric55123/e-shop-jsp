<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.cartitem.model.CartItem" %>

<%
    Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

    BigDecimal discount = (BigDecimal) session.getAttribute("discount");
    String appliedCouponCode = (String) session.getAttribute("appliedCouponCode");
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
    <%
        BigDecimal total = BigDecimal.ZERO;
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

<!-- 套用優惠券 -->
<br><br>
<form action="<%= request.getContextPath() %>/applyCoupon.action" method="post">
    <label for="couponCode">選擇優惠券：</label>
    <select name="couponCode" id="couponCode" required>
        <option value="">請選擇優惠券</option>
        <%
            List<com.eshop.coupon.model.CouponHolder> availableCoupons =
                    (List<com.eshop.coupon.model.CouponHolder>) session.getAttribute("availableCoupons");

            if (availableCoupons != null) {
                for (com.eshop.coupon.model.CouponHolder ch : availableCoupons) {
                    String code = ch.getCouponCode();
                    String name = ch.getCoupon().getName();
        %>
        <option value="<%= code %>" <%= code.equals(appliedCouponCode) ? "selected" : "" %>><%= name %></option>
        <%
                }
            }
        %>
    </select>
    <button type="submit">套用優惠券</button>
</form>

<% if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) { %>
<p>✅ 已套用優惠券：<strong><%= appliedCouponCode %></strong></p>
<p>✅ 折扣金額：<strong>$<%= discount %></strong></p>
<p><strong>優惠後總金額：</strong> $<%= total.subtract(discount) %></p>

<form action="<%= request.getContextPath() %>/removeCoupon.action" method="post">
    <button type="submit">取消優惠券</button>
</form>
<% } %>

<hr>
<h3>填寫收件資訊</h3>

<form action="<%= request.getContextPath() %>/checkout.action" method="post">
    <label>收件人姓名：</label><br>
    <input type="text" name="receiverName" required><br><br>

    <label>收件人電話：</label><br>
    <input type="text" name="receiverPhone" required><br><br>

    <label>收件人地址：</label><br>
    <input type="text" name="receiverAddress" required><br><br>

    <label>備註：</label><br>
    <textarea name="note" rows="4" cols="40"></textarea><br><br>

    <button type="submit">✅ 確認送出訂單</button>
</form>

<form action="<%= request.getContextPath() %>/clearCart.action" method="post" onsubmit="return doubleConfirmClear();">
    <button type="submit">🗑 清空購物車</button>
</form>
<% } %>

<br>
<a href="<%= request.getContextPath() %>/productList.action">← 回商品列表</a>
</body>
</html>
