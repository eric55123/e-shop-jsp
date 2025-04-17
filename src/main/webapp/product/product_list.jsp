<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>商品清單</title>
</head>
<body>

<!-- ✅ 登入狀態顯示 -->
<div style="text-align:right;">
    <c:choose>
        <c:when test="${not empty sessionScope.loginMember}">
            歡迎，
            <strong>
                <c:choose>
                    <c:when test="${not empty sessionScope.loginMember.name}">
                        ${sessionScope.loginMember.name}
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.loginMember.username}
                    </c:otherwise>
                </c:choose>
            </strong>！
            <a href="${pageContext.request.contextPath}/cart.action">前往購物車</a>
            <a href="memberProfile.action">會員資料</a> |
            <a href="logout.action">登出</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/cart.action">前往購物車</a>
            <a href="login.action">會員登入</a> |
            <a href="memberRegister.action">註冊</a>
        </c:otherwise>
    </c:choose>
</div>

<h2>商品清單</h2>

<p>
    <a href="faqList.action" target="_blank">❓ 常見問題</a>
</p>

<table border="1">
    <tr>
        <th>主圖</th>
        <th>編號</th>
        <th>名稱</th>
        <th>價格</th>
        <th>庫存</th>
        <th>狀態</th>
        <th>操作</th>
    </tr>

    <c:forEach var="product" items="${productList}">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${not empty product.coverImageUrl}">
                        <img src="https://drive.google.com/thumbnail?id=${fn:substringAfter(product.coverImageUrl, 'id=')}"
                             width="80" style="border:1px solid #ccc; padding:2px;" />
                    </c:when>
                    <c:otherwise>
                        <span style="color: gray;">無圖片</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${product.productNo}</td>
            <td>${product.productName}</td>
            <td>${product.productPrice}</td>
            <td>${product.remainingQty}</td>
            <td>
                <c:choose>
                    <c:when test="${product.productStatus == 1}">上架</c:when>
                    <c:otherwise>下架</c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="productDetail.action?productNo=${product.productNo}">查看</a>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="/back">🔙 回後台首頁</a>

</body>
</html>