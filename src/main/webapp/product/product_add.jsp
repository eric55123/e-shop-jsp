<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>新增商品</title>
</head>
<body>
<h2>新增商品</h2>

<s:form action="addProduct" method="post">
    <table>
        <tr>
            <td><label for="productName">商品名稱</label></td>
            <td><s:textfield name="product.productName" id="productName" required="true" size="30" /></td>
        </tr>

        <tr>
            <td><label for="productDesc">商品描述</label></td>
            <td><s:textarea name="product.productDesc" id="productDesc" rows="5" cols="30" /></td>
        </tr>

        <tr>
            <td><label for="productPrice">商品價格</label></td>
            <td><s:textfield name="product.productPrice" id="productPrice" required="true" size="10">
                <s:param name="type">number</s:param>
            </s:textfield></td>
        </tr>

        <tr>
            <td><label for="productAddQty">上架數量</label></td>
            <td><s:textfield name="product.productAddQty" id="productAddQty" required="true" size="10">
                <s:param name="type">number</s:param>
            </s:textfield></td>
        </tr>

        <tr>
            <td><label for="productStatus">商品狀態</label></td>
            <td>
                <s:select name="product.productStatus"
                          id="productStatus"
                          list="#{'1':'上架','0':'下架'}"
                          listKey="key"
                          listValue="value"
                          headerKey=""
                          headerValue="請選擇狀態" />
            </td>
        </tr>

        <tr>
            <td><label for="productCategory">商品類別</label></td>
            <td>
                <s:select name="product.productCategory"
                          id="productCategory"
                          list="categoryList"
                          listKey="id"
                          listValue="name"
                          headerKey=""
                          headerValue="請選擇類別" />
            </td>
        </tr>

        <tr>
            <td></td>
            <td><s:submit value="新增商品" /></td>
        </tr>
    </table>
</s:form>

</body>
</html>
