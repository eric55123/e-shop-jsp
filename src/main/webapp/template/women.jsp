<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>SH | Women</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<!-- CSS -->
	<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/sticky-navigation.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/demo.css" rel="stylesheet" />

	<!-- JS -->
	<script src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
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

<jsp:include page="/includes/header.jsp" />

<div class="container">
	<section id="main">
		<div class="content">
			<div class="pag-nav">
				<ul class="p-list">
					<li><a href="<s:url value='/index.jsp'/>">Home</a></li> &nbsp;/&nbsp;
					<li class="act">Women</li>
				</ul>
			</div>

			<%-- 每類商品區塊（從 action 傳入 coatsList, outerwearList, tshirtsList） --%>

			<c:if test="${not empty coatsList}">
				<div class="coats">
					<h3 class="c-head">Coats</h3>
					<small><a href="#">Show More</a></small>
					<div class="coat-row">
						<c:forEach var="p" items="${coatsList}">
							<div class="coat-column">
								<a href="productDetail.action?productNo=${p.productNo}">
									<img src="${pageContext.request.contextPath}/uploads/${p.mainImg}" class="img-responsive" alt="">
									<div class="prod-desc">
										<h4>${p.productName}</h4>
										<small>NT$ ${p.productPrice}</small>
									</div>
								</a>
								<div class="mask">
									<div class="info"><a href="productDetail.action?productNo=${p.productNo}">View</a></div>
								</div>
							</div>
						</c:forEach>
						<div class="clearfix"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${not empty outerwearList}">
				<div class="coats">
					<h3 class="c-head">Outerwear</h3>
					<small><a href="#">Show More</a></small>
					<div class="coat-row">
						<c:forEach var="p" items="${outerwearList}">
							<div class="coat-column">
								<a href="productDetail.action?productNo=${p.productNo}">
									<img src="${pageContext.request.contextPath}/uploads/${p.mainImg}" class="img-responsive" alt="">
									<div class="prod-desc">
										<h4>${p.productName}</h4>
										<small>NT$ ${p.productPrice}</small>
									</div>
								</a>
								<div class="mask">
									<div class="info"><a href="productDetail.action?productNo=${p.productNo}">View</a></div>
								</div>
							</div>
						</c:forEach>
						<div class="clearfix"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${not empty tshirtsList}">
				<div class="coats">
					<h3 class="c-head">T-shirts</h3>
					<small><a href="#">Show More</a></small>
					<div class="coat-row">
						<c:forEach var="p" items="${tshirtsList}">
							<div class="coat-column">
								<a href="productDetail.action?productNo=${p.productNo}">
									<img src="${pageContext.request.contextPath}/uploads/${p.mainImg}" class="img-responsive" alt="">
									<div class="prod-desc">
										<h4>${p.productName}</h4>
										<small>NT$ ${p.productPrice}</small>
									</div>
								</a>
								<div class="mask">
									<div class="info"><a href="productDetail.action?productNo=${p.productNo}">View</a></div>
								</div>
							</div>
						</c:forEach>
						<div class="clearfix"></div>
					</div>
				</div>
			</c:if>

			<div class="look">
				<h3>Check our lookbook</h3>
			</div>

			<!-- Partner 輪播 -->
			<div class="partner">
				<ul id="flexiselDemo3">
					<li><img src="${pageContext.request.contextPath}/images/ss1.jpg" class="img-responsive" /></li>
					<li><img src="${pageContext.request.contextPath}/images/ss2.jpg" class="img-responsive" /></li>
					<li><img src="${pageContext.request.contextPath}/images/ss3.jpg" class="img-responsive" /></li>
					<li><img src="${pageContext.request.contextPath}/images/ss4.jpg" class="img-responsive" /></li>
					<li><img src="${pageContext.request.contextPath}/images/ss5.png" class="img-responsive" /></li>
				</ul>
			</div>
		</div>
	</section>
</div>

<jsp:include page="/includes/footer.jsp" />

</body>
</html>
