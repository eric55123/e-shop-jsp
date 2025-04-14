<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<html>
<head>
    <title>管理後台</title>
</head>
<body>

<%
    Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
    if (loggedInAdmin == null) {
        response.sendRedirect(request.getContextPath() + "/adminLogin.action");
        return;
    }

    boolean isSuper = "super".equalsIgnoreCase(loggedInAdmin.getRole());
%>

<h2>管理後台</h2>

<p>
    👤 歡迎，<strong><%= loggedInAdmin.getName() %></strong>（帳號：<%= loggedInAdmin.getUsername() %>） |
    <a href="adminLogout.action">🚪 登出</a>
</p>

<ul>
    <li><a href="coupon/addCoupon.action">➕ 新增優惠券</a></li>
    <li><a href="listCoupons.action">📋 查看所有優惠券</a></li>
    <li><a href="assignCouponSelect.action">🎁 發放優惠券</a></li>
    <li><a href="adminCommentReport.action">🛡️ 評論檢舉審核</a></li>
    <li><a href="faqAdminList.action">❓ 常見問題管理</a></li>

    <% if (isSuper) { %>
    <li><a href="adminCreate.action">👑 新增管理員</a></li>
    <% } %>
</ul>

</body>
</html>
