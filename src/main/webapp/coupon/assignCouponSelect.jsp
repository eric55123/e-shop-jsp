<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>選擇要發放的優惠券</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>

<h2>🎯 請選擇要發放的優惠券</h2>

<table>
    <tr>
        <th>編號</th>
        <th>名稱</th>
        <th>折扣類型</th>
        <th>折扣</th>
        <th>門檻</th>
        <th>有效期限</th>
        <th>操作</th>
    </tr>

    <s:iterator value="couponList">
        <tr>
            <td><s:property value="couponId"/></td>
            <td><s:property value="name"/></td>
            <td><s:property value="discountType"/></td>
            <td><s:property value="discountValue"/></td>
            <td><s:property value="minSpend"/></td>
            <td><s:property value="validFrom"/> ~ <s:property value="validTo"/></td>
            <td>
                <s:url var="assignUrl" action="showAssignForm">
                    <s:param name="coupon.couponId" value="couponId"/>
                </s:url>
                <a href="${assignUrl}">🎁 發放</a>
            </td>
        </tr>
    </s:iterator>
</table>

<br>
<a href="<%= request.getContextPath() %>/back" class="btn btn-secondary">🔙 回後台首頁</a>


</body>
</html>
