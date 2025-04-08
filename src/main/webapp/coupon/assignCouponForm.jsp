<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>發放優惠券</title>
</head>
<body>
<h2>📨 發放優惠券</h2>

<!-- ✅ 顯示發放成功或錯誤訊息 -->
<s:if test="hasActionErrors()">
    <div style="color: red;"><s:actionerror/></div>
</s:if>

<s:if test="message != null">
    <div style="color: green;"><s:property value="message"/></div>
</s:if>

<!-- ✅ 顯示優惠券資訊 -->
<p>優惠券代碼：<b><s:property value="coupon.couponCode"/></b></p>
<p>優惠券名稱：<b><s:property value="coupon.name"/></b></p>

<!-- ✅ 發放表單 -->
<s:form action="assignCoupon">
    <s:hidden name="coupon.couponId"/>

    <label>選擇要發放的會員：</label><br>
    <s:select
            name="memberId"
            list="memberList"
            listKey="memberId"
            listValue="name"
            label="會員"
            headerKey=""
            headerValue="請選擇會員"/>

    <br><br>
    <s:submit value="✅ 發放"/>
</s:form>

<br>
<a href="listCoupons.action">🔙 返回優惠券清單</a>
</body>
</html>
