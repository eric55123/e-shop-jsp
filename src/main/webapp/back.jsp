<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>管理後台</title>
</head>
<body>
<h2>管理後台</h2>

<ul>
    <li><a href="coupon/addCoupon.action">➕ 新增優惠券</a></li>
    <li><a href="listCoupons.action">📋 查看所有優惠券</a></li>
    <li><a href="assignCouponSelect.action">🎁 發放優惠券</a></li> <!-- ✅ 新增這行 -->
    <!-- 你之後可以加入更多像這樣的連結 -->
    <!-- <li><a href="couponStat.jsp">📊 優惠券統計</a></li> -->
</ul>
</body>
</html>
