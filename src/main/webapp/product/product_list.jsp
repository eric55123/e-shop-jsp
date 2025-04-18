<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>商品清單</title>
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" type="text/css" />
    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" type="text/css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" type="text/css" />
</head>
<body>

<!-- ✅ 導覽列 -->
<div class="header">
    <div class="container">
        <div id="demo_top_wrapper">
            <div id="sticky_navigation_wrapper">
                <div id="sticky_navigation">
                    <div class="demo_container navigation-bar">
                        <div class="navigation">
                            <div class="logo"><a href="productList.action">eShop</a></div>
                            <span class="menu"></span>
                            <div class="navig">
                                <ul>
                                    <li><a href="women.action">Woman</a></li>
                                    <li><a href="men.action">Men</a></li>
                                    <li><a href="#">Kids</a></li>
                                    <li><a href="#">即將到來</a></li>
                                    <li><a href="faqList.action">常見問題</a></li>
                                </ul>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div class="navigation-right">
                            <ul class="user">
                                <li><span></span><a href="login.action">Log In</a></li>
                                <li><span class="cart"></span><a href="cart.action">購物車</a></li>
                                <li><button class="search"></button></li>
                            </ul>
                        </div>
                        <div class="clearfix"></div>
                        <div class="serch">
                            <span>
                                <input type="text" placeholder="Search" required="">
                                <input type="submit" value="" />
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ✅ 商品清單主內容 -->
<div class="container mt-5">
    <section id="main">
        <div class="content">
            <h2 class="text-center mb-4">商品清單</h2>

            <p class="text-center text-muted">
                第 ${pageNo} 頁，共 ${totalPages} 頁
            </p>
            <div class="row mb-3">
                <div class="col-md-6">
                    <form action="listByCategory.action" method="get" class="form-inline">
                        <input type="hidden" name="pageNo" value="1" />
                        <label for="categorySelect" class="mr-2">分類：</label>
                        <select name="product.productCategory.productCategoryId" id="categorySelect" class="form-control" onchange="this.form.submit()">
                            <option value="">全部分類</option>
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

            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="thead-dark">
                    <tr>
                        <th>主圖</th>
                        <th>編號</th>
                        <th>名稱</th>
                        <th>價格</th>
                        <th>庫存</th>
                        <th>狀態</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
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
                                    <span class="badge <c:choose><c:when test='${product.productStatus == 1}'>badge-success</c:when><c:otherwise>badge-secondary</c:otherwise></c:choose>">
                                        <c:choose>
                                            <c:when test="${product.productStatus == 1}">上架</c:when>
                                            <c:otherwise>下架</c:otherwise>
                                        </c:choose>
                                    </span>
                            </td>
                            <td>
                                <a href="productDetail.action?productNo=${product.productNo}" class="btn btn-primary btn-sm">查看</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- ✅ 分頁控制 -->
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

            </div>

            <div class="text-right mt-3">
                <a href="/back" class="btn btn-secondary">🔙 回後台首頁</a>
            </div>
        </div>
    </section>
</div>

</body>
</html>