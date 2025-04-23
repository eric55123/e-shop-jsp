<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/slider.jsp" />

<!-- 自訂樣式 -->
<style>
    .product__list .product {
        transition: all 0.3s ease-in-out;
        cursor: pointer;
    }

    .product__list .product:hover {
        transform: scale(1.03);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
    }

    .product__list .product__thumb img {
        transition: all 0.3s ease;
    }

    .product__list .product:hover .product__thumb img {
        filter: brightness(90%);
    }

    .product__details a:hover {
        color: #e74c3c;
        text-decoration: underline;
    }
</style>

<!-- 商品清單主內容 -->
<div class="container mt-5">
    <section id="main">
        <div class="content">
            <h2 class="text-center mb-4">商品列表</h2>

            <p class="text-center text-muted">
                第 ${pageNo} 頁，共 ${totalPages} 頁
            </p>

            <!-- 分類篩選 -->
            <div class="row mb-3">
                <div class="col-md-6">
                    <form action="listByCategory.action" method="get" class="form-inline">
                        <input type="hidden" name="pageNo" value="1" />
                        <label for="categorySelect" class="mr-2">分類：</label>
                        <select name="product.productCategory.productCategoryId" id="categorySelect" class="form-control" onchange="this.form.submit()">
                            <option value="">全部商品</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.productCategoryId}"
                                        <c:if test="${product.productCategory.productCategoryId == cat.productCategoryId}">selected</c:if>>
                                        ${cat.productCategoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </div>

            <!-- 商品卡片清單 -->
            <div class="row product__list gx-4 gy-4">
            <c:forEach var="product" items="${productList}">
                    <div class="col-md-3 col-lg-3 col-sm-6 mb-4 single__pro">
                        <a href="productDetail.action?productNo=${product.productNo}" class="text-decoration-none text-dark">
                            <div class="product foo border rounded p-2 shadow-sm h-100 d-flex flex-column">
                                <div class="product__inner text-center">
                                    <div class="pro__thumb mb-2">
                                        <c:choose>
                                            <c:when test="${not empty product.coverImageUrl}">
                                                <img src="https://drive.google.com/thumbnail?id=${fn:substringAfter(product.coverImageUrl, 'id=')}"
                                                     alt="${product.productName}" class="img-fluid" style="max-height: 160px;">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">無圖片</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="product__details text-center mt-auto">
                                    <h2 class="h6 mb-2">
                                            ${product.productName}
                                    </h2>
                                    <ul class="product__price list-unstyled mb-2">
                                        <li class="new__price">$${product.productPrice}</li>
                                    </ul>
                                    <span class="badge
                                        <c:choose>
                                            <c:when test='${product.productStatus == 1}'>badge-success</c:when>
                                            <c:otherwise>badge-secondary</c:otherwise>
                                        </c:choose>">
                                        <c:choose>
                                            <c:when test="${product.productStatus == 1}">上架</c:when>
                                            <c:otherwise>下架</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>

            <!-- 分頁控制 -->
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center mt-4">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item <c:if test='${i == pageNo}'>active</c:if>">
                            <c:choose>
                                <c:when test="${not empty product.productCategory.productCategoryId}">
                                    <a class="page-link"
                                       href="listByCategory.action?pageNo=${i}&product.productCategory.productCategoryId=${product.productCategory.productCategoryId}">
                                            ${i}
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a class="page-link" href="productList.action?pageNo=${i}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>
                </ul>
            </nav>

            <div class="text-right mt-3">
                <a href="<%= request.getContextPath() %>/back" class="btn btn-secondary">🔙 回後台首頁</a>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/includes/footer.jsp" />
