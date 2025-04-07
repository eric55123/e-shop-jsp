<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>商品清單</title>
</head>
<body>

<!-- ✅ 登入狀態顯示 -->
<div style="text-align:right;">
    <s:if test="#session.loginMember != null">
        歡迎，<strong>
        <s:if test="#session.loginMember.loginType == 'local'">
            <s:property value="#session.loginMember.username" />
        </s:if>
        <s:else>
            <s:property value="#session.loginMember.name" />
        </s:else>
        </strong>！
        <a href="<%= request.getContextPath() %>/cartitem/cart.jsp">🛒 查看購物車</a>
        <a href="memberProfile.action">會員資料</a> |
        <a href="logout.action">登出</a>
    </s:if>
    <s:else>
        <a href="<%= request.getContextPath() %>/cartitem/cart.jsp">🛒 查看購物車</a>
        <a href="login.action">會員登入</a> |
        <a href="memberRegister.action">註冊</a>
    </s:else>
</div>


<h2>商品清單</h2>

<!-- 新增商品按鈕放最上面 -->
<p><a href="showAddProduct.action">➕ 新增商品</a></p>

<table border="1">
    <tr>
        <th>編號</th>
        <th>名稱</th>
        <th>價格</th>
        <th>庫存</th>
        <th>狀態</th>
        <th>操作</th>
    </tr>
    <s:iterator value="productList">
        <tr>
            <td><s:property value="productNo" /></td>
            <td><s:property value="productName" /></td>
            <td><s:property value="productPrice" /></td>
            <td><s:property value="remainingQty" /></td>
            <td>
                <s:if test="productStatus == 1">上架</s:if>
                <s:if test="productStatus == 0">下架</s:if>
            </td>
            <td>
                <a href="<s:url action='productDetail'>
                    <s:param name='productNo' value='productNo'/>
                </s:url>">查看</a> |
                <a href="<s:url action='editProduct'>
                    <s:param name='productNo' value='productNo'/>
                </s:url>">修改</a> |
                <a href="<s:url action='deleteProduct'>
                    <s:param name='productNo' value='productNo'/>
                </s:url>"
                   onclick="return confirm('確定要刪除這筆商品嗎？')">刪除</a>
            </td>
        </tr>
    </s:iterator>
</table>

</body>
</html>