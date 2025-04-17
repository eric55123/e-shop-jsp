<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title><s:property value="product.productName" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<!-- CSS -->
	<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/demo1.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/imagezoom.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/es-cus.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/sticky-navigation.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/demo.css" rel="stylesheet" />

	<!-- JS -->
	<script src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.imagezoom.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/modernizr.custom.17475.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.elastislide.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.flexisel.js"></script>

	<script>
		$(function () {
			var sticky_navigation_offset_top = $('#sticky_navigation').offset().top;
			var sticky_navigation = function () {
				var scroll_top = $(window).scrollTop();
				if (scroll_top > sticky_navigation_offset_top) {
					$('#sticky_navigation').css({ 'position': 'fixed', 'top': 0, 'left': 0 });
				} else {
					$('#sticky_navigation').css({ 'position': 'relative' });
				}
			};
			sticky_navigation();
			$(window).scroll(function () {
				sticky_navigation();
			});
		});

		$(window).load(function () {
			$("#flexiselDemo3").flexisel({
				visibleItems: 5,
				animationSpeed: 1000,
				autoPlay: true,
				autoPlaySpeed: 3000,
				pauseOnHover: true,
				enableResponsiveBreakpoints: true,
				responsiveBreakpoints: {
					portrait: { changePoint: 480, visibleItems: 1 },
					landscape: { changePoint: 640, visibleItems: 2 },
					tablet: { changePoint: 768, visibleItems: 3 }
				}
			});
		});
	</script>
</head>
<body>

<jsp:include page="/includes/header.jsp"/>

<div class="container">
	<section id="main">
		<div class="content">
			<div class="pag-nav">
				<ul class="p-list">
					<li><a href="<s:url value='/index.jsp'/>">Home</a></li>
					<li><a href="<s:url value='/productList.action'/>">商品列表</a></li>
					<li class="act"><s:property value="product.productName" /></li>
				</ul>
			</div>

			<div class="page">
				<!-- 主圖與輪播 -->
				<div class="box cf">
					<div class="left">
                        <span class="demowrap">
                            <img id="demo2" class="imagezoom" src="${pageContext.request.contextPath}/uploads/<s:property value='product.mainImg' />" />
                        </span>
						<ul id="demo2carousel" class="elastislide-list">
							<c:forEach var="img" items="${product.productImgs}">
								<li>
									<a href="#"><img src="${pageContext.request.contextPath}/uploads/${img.imgUrl}" data-largeimg="${pageContext.request.contextPath}/uploads/${img.imgUrl}" /></a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>

				<div class="coats sing-c">
					<h3 class="c-head"><s:property value="product.productName" /></h3>
					<p class="article">商品編號: <s:property value="product.productNo" /></p>
					<p>NT$ <s:property value="product.productPrice" /></p>
					<p class="art"><s:property value="product.productDesc" /></p>

					<div class="size">
						<small>數量：</small>
						<form action="<s:url value='/addToCart.action' />" method="post">
							<input type="hidden" name="productNo" value="<s:property value='product.productNo' />" />
							<input type="number" name="quantity" value="1" min="1" max="<s:property value='product.remainingQty' />" />
							<button type="submit">加入購物車</button>
						</form>
					</div>
				</div>

				<div class="look">
					<h3>You May Also Like</h3>
				</div>

				<!-- 推薦商品輪播 -->
				<div class="partner flexy">
					<ul id="flexiselDemo3">
						<c:forEach var="p" items="${relatedProducts}">
							<li>
								<a href="productDetail.action?productNo=${p.productNo}">
									<img src="${pageContext.request.contextPath}/uploads/${p.mainImg}" class="img-responsive" alt=""/>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</section>
</div>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>
