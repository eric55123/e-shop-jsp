<%@ page contentType="text/html; charset=UTF-8" %>
<%
    if (session.getAttribute("loginMember") == null) {
        response.sendRedirect("login.action");
        return;
    }
%>

<html>
<head>
    <title>填寫收件資訊</title>
</head>
<body>
<h2>請填寫收件資訊</h2>

<form action="checkout.action" method="post">
    <label>收件人姓名：</label><br>
    <input type="text" name="receiverName" required><br><br>

    <label>收件人電話：</label><br>
    <input type="text" name="receiverPhone" required><br><br>

    <label>收件人地址：</label><br>
    <input type="text" name="receiverAddress" required><br><br>

    <label>備註：</label><br>
    <textarea name="note" rows="4" cols="40"></textarea><br><br>

    <button type="submit">確認送出</button>
</form>

<br>
<a href="cartitem/cart.jsp">← 回購物車</a>
</body>
</html>
