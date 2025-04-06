<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.product.model.ProductComment" %>
<%@ page import="com.eshop.member.Model.Member" %>
<%@ page import="java.util.*" %>

<%
    Product product = (Product) request.getAttribute("product");
    List<ProductComment> comments = (List<ProductComment>) request.getAttribute("comments");
    Member loginMember = (Member) session.getAttribute("loginMember");
    Set<Integer> reportedCommentIds = (Set<Integer>) request.getAttribute("reportedCommentIds");
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

<hr>
<h3>評論區</h3>

<!-- 所有評論列表 -->
<% if (comments != null && !comments.isEmpty()) { %>
<ul>
    <% for (ProductComment comment : comments) { %>
    <li>
        ⭐ <%= comment.getRating() %> 顆星 -
        <strong><%= comment.getMember().getUsername() != null
                ? comment.getMember().getUsername()
                : comment.getMember().getName() %></strong>：
        <%= comment.getCommentText() %><br>
        <small>🕓 <%= comment.getCommentTime() %></small>

        <% if (loginMember != null) {
            boolean alreadyReported = reportedCommentIds != null && reportedCommentIds.contains(comment.getCommentId());
            if (!alreadyReported) { %>
        <form action="reportComment.action" method="post" style="display:inline; margin-left: 10px;">
            <input type="hidden" name="commentId" value="<%= comment.getCommentId() %>">
            <select name="reason" required>
                <option value="">檢舉原因</option>
                <option value="辱罵">辱罵</option>
                <option value="廣告">廣告</option>
                <option value="歧視">歧視</option>
                <option value="其他">其他</option>
            </select>
            <button type="submit">檢舉</button>
        </form>
        <%   } else { %>
        <span style="color:gray; margin-left: 10px;">您已檢舉</span>
        <%   }
        } %>
    </li>
    <% } %>
</ul>
<% } else { %>
<p>目前尚無評論。</p>
<% } %>

<!-- 登入才能留言 -->
<% if (loginMember != null) { %>
<hr>
<h4>發表評論</h4>
<form action="addProductComment.action" method="post">
    <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
    <p>
        評分：
        <select name="rating" required>
            <option value="">請選擇</option>
            <option value="5">5 顆星</option>
            <option value="4">4 顆星</option>
            <option value="3">3 顆星</option>
            <option value="2">2 顆星</option>
            <option value="1">1 顆星</option>
        </select>
    </p>
    <p>
        評論內容：<br>
        <textarea name="commentText" rows="4" cols="40" required></textarea>
    </p>
    <button type="submit">送出評論</button>
</form>
<% } else { %>
<p>請先 <a href="login.jsp">登入</a> 才能留言。</p>
<% } %>

<% } else { %>
<p>查無此商品。</p>
<% } %>

<br>
<a href="productList.action">← 回商品列表</a>
</body>
</html>
