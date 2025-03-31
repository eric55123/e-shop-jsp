<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>修改商品</title>
</head>
<body>
<h2>修改商品</h2>

<s:form action="updateProduct" method="post">
    <!-- 隱藏欄位：商品編號 -->
    <s:hidden name="product.productNo" />

    <table>
        <tr>
            <td><label for="productName">商品名稱</label></td>
            <td><s:textfield name="product.productName" id="productName" size="30" required="true" /></td>
        </tr>

        <tr>
            <td><label for="productDesc">商品描述</label></td>
            <td><s:textarea name="product.productDesc" id="productDesc" rows="5" cols="30" /></td>
        </tr>

        <tr>
            <td><label for="productPrice">商品價格</label></td>
            <td><s:textfield name="product.productPrice" id="productPrice" size="10" required="true" /></td>
        </tr>

        <tr>
            <td><label for="productAddQty">上架數量</label></td>
            <td><s:textfield name="product.productAddQty" id="productAddQty" size="10" required="true" /></td>
        </tr>

        <tr>
            <td><label for="productStatus">商品狀態</label></td>
            <td>
                <s:select name="product.productStatus"
                          id="productStatus"
                          list="statusOptions"
                          listKey="key"
                          listValue="value"
                          headerKey=""
                          headerValue="請選擇狀態" />
            </td>
        </tr>

        <tr>
            <td><label for="productCategory">商品類別</label></td>
            <td>
                <s:select name="product.productCategory.productCategoryId"
                          id="productCategory"
                          list="categoryList"
                          listKey="productCategoryId"
                          listValue="productCategoryName"
                          headerKey=""
                          headerValue="請選擇類別" />
            </td>
        </tr>

        <tr>
            <td></td>
            <td><s:submit value="更新商品" /></td>
        </tr>
    </table>
</s:form>

<p><a href="productList.action">← 回商品列表</a></p>
</body>
</html>
