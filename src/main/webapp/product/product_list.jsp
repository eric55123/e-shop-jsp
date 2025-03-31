<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>商品清單</title>
</head>
<body>
<h2>商品清單</h2>

<table border="1">
    <tr>
        <th>編號</th>
        <th>名稱</th>
        <th>價格</th>
        <th>庫存</th>
        <th>操作</th> <!-- 新增 -->
    </tr>
    <s:iterator value="productList">
        <tr>
            <td><s:property value="productNo" /></td>
            <td><s:property value="productName" /></td>
            <td><s:property value="productPrice" /></td>
            <td><s:property value="remainingQty" /></td>
            <td>
                <a href="productDetail.action?productNo=<s:property value='productNo'/>">查看詳情</a>
                <a href="showAddProduct.action">新增商品</a>

            </td>
        </tr>
    </s:iterator>
</table>

</body>
</html>
