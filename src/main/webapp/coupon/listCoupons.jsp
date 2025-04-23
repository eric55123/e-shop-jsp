<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>優惠券列表</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>

<h2>📋 所有優惠券清單</h2>

<table>
    <tr>
        <th>編號</th>
        <th>名稱</th>
        <th>類型</th>
        <th>折扣</th>
        <th>門檻</th>
        <th>生效時間</th>
        <th>結束時間</th>
        <th>啟用</th>
        <th>操作</th> <!-- ✅ 新增 -->
    </tr>

    <s:iterator value="couponList">
        <tr>
            <td><s:property value="couponId"/></td>
            <td><s:property value="name"/></td>
            <td><s:property value="discountType"/></td>
            <td><s:property value="discountValue"/></td>
            <td><s:property value="minSpend"/></td>
            <td><s:property value="validFrom"/></td>
            <td><s:property value="validTo"/></td>
            <td>
                <s:if test="isEnabled == 1">
                    ✅
                </s:if>
                <s:else>
                    ❌
                </s:else>
            </td>
            <td>
                <a href="editCoupon.action?coupon.couponId=<s:property value='couponId'/>">✏️ 修改</a>
                |
                <s:url var="deleteUrl" action="deleteCoupon">
                    <s:param name="coupon.couponId" value="couponId"/>
                </s:url>
                <a href="${deleteUrl}" onclick="return confirm('確定要刪除這張優惠券嗎？')">🗑️ 刪除</a>
            </td>
        </tr>
    </s:iterator>

</table>

<br>
<a href="<%= request.getContextPath() %>/back" class="btn btn-secondary">🔙 回後台首頁</a>

</body>
</html>
