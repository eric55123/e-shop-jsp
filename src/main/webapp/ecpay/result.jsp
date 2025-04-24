<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>

<%
    String rtnCode = request.getParameter("RtnCode"); // 綠界回傳代碼，1 為成功
    String tradeNo = request.getParameter("MerchantTradeNo");
    String paymentDate = request.getParameter("PaymentDate");
    String totalAmount = request.getParameter("TradeAmt");
%>

<html>
<head>
    <title>付款結果</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 40px;
        }
        .result-box {
            border: 2px solid #ccc;
            border-radius: 10px;
            padding: 30px;
            max-width: 600px;
            margin: auto;
            background-color: #f9f9f9;
        }
        .success {
            color: green;
            font-size: 1.5em;
        }
        .fail {
            color: red;
            font-size: 1.5em;
        }
    </style>
</head>
<body>

<div class="result-box">
    <% if ("1".equals(rtnCode)) { %>
    <div class="success">✅ 付款成功！</div>
    <p>訂單編號：<strong><%= tradeNo %></strong></p>
    <p>付款時間：<%= paymentDate %></p>
    <p>付款金額：<strong>$<%= totalAmount %></strong></p>
    <p>感謝您的購買！我們將盡快出貨。</p>
    <% } else { %>
    <div class="fail">❌ 付款失敗或取消！</div>
    <p>若您遇到問題，請聯繫客服協助處理。</p>
    <% } %>

    <br>
    <a href="productList.action">回到首頁</a>
</div>

</body>
</html>
