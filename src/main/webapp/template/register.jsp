
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
	<title>Shade Bootstrap Website Template | Register</title>
	<link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
	<script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>
	<link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" type="text/css" media="all" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="text/javascript">
		addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		function hideURLbar(){ window.scrollTo(0,1); }
	</script>
	<link href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700,900' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
	<link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="header">
	<div class="container">
		<div id="demo_top_wrapper">
			<div id="sticky_navigation_wrapper">
				<div id="sticky_navigation">
					<div class="demo_container navigation-bar">
						<div class="navigation">
							<div class="logo"><a href="home.action">SH</a></div>
							<span class="menu"></span>
							<script>$( "span.menu" ).click(function() { $( ".navig" ).slideToggle( "slow"); });</script>
							<div class="navig">
								<ul>
									<li><a href="women.action">Woman</a></li>
									<li><a href="men.action">Men</a></li>
									<li><a href="#">Kids</a></li>
									<li><a href="#">Coming Soon</a></li>
									<li><a href="about.action">About</a></li>
								</ul>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="navigation-right">
							<ul class="user">
								<li><span></span><a href="login.action">Log In</a></li>
								<li><span class="bascket"></span><a href="bascket.action">Basket(0)</a></li>
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
						<script>$( "button.search" ).click(function() { $( ".serch" ).slideToggle( "slow"); });</script>
					</div></div></div></div></div></div>

<div class="container">
	<section id="main">
		<div class="content">
			<div class="pag-nav">
				<ul class="p-list">
					<li><a href="home.action">Home</a></li> &nbsp;&nbsp;/&nbsp;
					<li class="act">&nbsp;Register</li>
				</ul>
			</div>
			<div class="coats">
				<h3 class="c-head">Register Here</h3>
				<p>Lorem ipsum dolor sit amet enim. Etiam ullamcorp uspendisse a pellentesque.</p>
			</div>
			<div class="register">
				<form>
					<div class="register-top-grid">
						<h3>PERSONAL INFORMATION</h3>
						<div><span>First Name<label>*</label></span><input type="text"></div>
						<div><span>Last Name<label>*</label></span><input type="text"></div>
						<div><span>Email Address<label>*</label></span><input type="text"></div>
						<div class="clearfix"></div>
						<a class="news-letter" href="#"><label class="checkbox"><input type="checkbox" name="checkbox" checked=""><i> </i>Sign Up for Newsletter</label></a>
					</div>
					<div class="register-bottom-grid">
						<h3>LOGIN INFORMATION</h3>
						<div><span>Password<label>*</label></span><input type="password"></div>
						<div><span>Confirm Password<label>*</label></span><input type="password"></div>
					</div>
				</form>
				<div class="clearfix"></div>
				<div class="register-but">
					<form><input type="submit" value="submit"><div class="clearfix"></div></form>
				</div>
			</div>

			<div class="look"><h3>You May Also Like</h3></div>

			<div class="partner">
				<ul id="flexiselDemo3">
					<li><img src="<%=request.getContextPath()%>/template/images/ss1.jpg" class="img-responsive" alt=""/></li>
					<li><img src="<%=request.getContextPath()%>/template/images/ss2.jpg" class="img-responsive" alt=""/></li>
					<li><img src="<%=request.getContextPath()%>/template/images/ss3.jpg" class="img-responsive" alt=""/></li>
					<li><img src="<%=request.getContextPath()%>/template/images/ss4.jpg" class="img-responsive" alt=""/></li>
					<li><img src="<%=request.getContextPath()%>/template/images/ss5.png" class="img-responsive" alt=""/></li>
				</ul>
				<script src="<%=request.getContextPath()%>/template/js/jquery.flexisel.js"></script>
				<script>
					$(window).load(function() {
						$("#flexiselDemo3").flexisel({
							visibleItems: 5,
							animationSpeed: 1000,
							autoPlay: true,
							autoPlaySpeed: 3000,
							pauseOnHover: true,
							enableResponsiveBreakpoints: true,
							responsiveBreakpoints: {
								portrait: { changePoint:480, visibleItems: 1 },
								landscape: { changePoint:640, visibleItems: 2 },
								tablet: { changePoint:768, visibleItems: 3 }
							}
						});
					});
				</script>
			</div>

			<div class="footer">
				<div class="row footer-row">
					<div class="col-md-3 footer-col">
						<h3 class="ft-title">Collection</h3>
						<ul class="ft-list">
							<li><a href="#">Woman (1725)</a></li>
							<li><a href="#">Men (635)</a></li>
							<li><a href="#">Kids (2514)</a></li>
							<li><a href="#">Coming Soon (76)</a></li>
						</ul>
					</div>
					<div class="col-md-3 footer-col">
						<h3 class="ft-title">Site</h3>
						<ul class="ft-list list-h">
							<li><a href="#">Terms of Service</a></li>
							<li><a href="#">Privacy Policy</a></li>
							<li><a href="#">Copyright Policy</a></li>
							<li><a href="#">Press Kit</a></li>
							<li><a href="#">Support</a></li>
						</ul>
					</div>
					<div class="col-md-3 footer-col">
						<h3 class="ft-title">Shop</h3>
						<ul class="ft-list list-h">
							<li><a href="#">About us</a></li>
							<li><a href="#">Shipping Methods</a></li>
							<li><a href="#">Career</a></li>
							<li><a href="contact.action">Contact</a></li>
						</ul>
					</div>
					<div class="col-md-3 foot-cl">
						<h3 class="ft-title">Social</h3>
						<p>Shoper is made with love in Warsaw,<br>2014 &copy; <a target="_blank" href="http://www.mobanwang.com/" title="网页模板">网页模板</a></p>
						<ul class="social">
							<li><i class="fa"></i></li>
							<li><i class="tw"></i></li>
							<li><i class="is"></i></li>
						</ul>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
	</section>
</div>
</body>
</html>