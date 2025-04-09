<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>發放優惠券</title>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
</head>
<body>
<h2>📨 發放優惠券</h2>

<!-- ✅ 訊息 -->
<s:if test="hasActionErrors()">
    <div style="color: red;"><s:actionerror/></div>
</s:if>
<s:if test="message != null">
    <div style="color: green;"><s:property value="message"/></div>
</s:if>

<!-- ✅ 優惠券資訊 -->
<p>優惠券代碼：<b><s:property value="coupon.couponCode"/></b></p>
<p>優惠券名稱：<b><s:property value="coupon.name"/></b></p>

<!-- ✅ 篩選查詢表單 -->
<form method="get" action="showAssignForm.action">
    <input type="hidden" name="coupon.couponId" value="<s:property value='coupon.couponId'/>"/>

    <label>篩選條件：</label>
    <select name="filterType" id="filterType" onchange="toggleFilterFields()">
        <option value="all" <s:if test='filterType == "all"'>selected</s:if>>全部會員</option>
        <option value="birthday" <s:if test='filterType == "birthday"'>selected</s:if>>本月壽星</option>
        <option value="registerRange" <s:if test='filterType == "registerRange"'>selected</s:if>>註冊時間區間</option>
    </select>

    <label style="margin-left:10px;">關鍵字：</label>
    <input type="text" name="keyword" value="${keyword}" placeholder="姓名或帳號"/>

    <div id="dateRangeFields" style="margin-top: 10px; display: none;">
        <label>開始註冊日：</label>
        <input type="date" name="startDate" value="${startDate}"/>
        <label>結束註冊日：</label>
        <input type="date" name="endDate" value="${endDate}"/>
    </div>

    <button type="submit">🔍 查詢</button>
</form>

<br>

<!-- ✅ 單一或多選會員發放 -->
<form action="assignCoupon.action" method="post" onsubmit="return confirm('是否確定要發放給勾選的會員？');">
    <input type="hidden" name="coupon.couponId" value="<s:property value='coupon.couponId'/>"/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>選擇</th>
            <th>會員編號</th>
            <th>姓名</th>
            <th>帳號</th>
            <th>信箱</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="member" items="${memberList}" varStatus="status">
            <tr>
                <td><input type="checkbox" name="memberIds" value="${member.memberId}"
                           <c:if test="${status.first}">checked</c:if> /></td>
                <td>${member.memberId}</td>
                <td>${member.name}</td>
                <td>${member.username}</td>
                <td>${member.email}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br>
    <input type="submit" value="✅ 發放選擇會員"/>
</form>

<!-- ✅ 後端安全方式全部發送 -->
<form action="assignSelectedCoupon.action" method="post" onsubmit="return confirm('是否發放給所有符合篩選條件的會員？');">
    <input type="hidden" name="coupon.couponId" value="<s:property value='coupon.couponId'/>"/>
    <input type="hidden" name="filterType" value="${filterType}"/>
    <input type="hidden" name="startDate" value="${startDate}"/>
    <input type="hidden" name="endDate" value="${endDate}"/>
    <input type="hidden" name="keyword" value="${keyword}"/>
    <button type="submit">📤 全部發送（符合篩選）</button>
</form>

<br>
<a href="listCoupons.action">🔙 返回優惠券清單</a>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script>
    $(function () {
        toggleFilterFields();
    });

    function toggleFilterFields() {
        const selected = document.getElementById("filterType").value;
        document.getElementById("dateRangeFields").style.display = selected === "registerRange" ? "block" : "none";
    }
</script>
</body>
</html>
