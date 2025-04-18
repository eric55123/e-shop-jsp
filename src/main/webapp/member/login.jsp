<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>會員登入</title>

    <!-- ✅ Bootstrap & 自訂樣式 -->
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" type="text/css" />
    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>

    <script>
        function validateLogin() {
            const email = document.querySelector('[name="email"]').value.trim();
            const password = document.querySelector('[name="password"]').value.trim();

            if (email === "") {
                alert("請輸入 Email！");
                return false;
            }

            const emailRegex = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
            if (!emailRegex.test(email)) {
                alert("請輸入正確的 Email 格式！");
                return false;
            }

            if (password === "") {
                alert("請輸入密碼！");
                return false;
            }

            return true;
        }

        $(function() {
            var sticky_navigation_offset_top = $('#sticky_navigation').offset()?.top || 0;
            var sticky_navigation = function(){
                var scroll_top = $(window).scrollTop();
                if (scroll_top > sticky_navigation_offset_top) {
                    $('#sticky_navigation').css({ 'position': 'fixed', 'top':0, 'left':0 });
                } else {
                    $('#sticky_navigation').css({ 'position': 'relative' });
                }
            };
            sticky_navigation();
            $(window).scroll(function() { sticky_navigation(); });
            $('a[href="#"]').click(function(event){ event.preventDefault(); });
        });
    </script>
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
                            <script>
                                $("span.menu").click(function() {
                                    $(".navig").slideToggle("slow");
                                });
                            </script>
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
                        <script>
                            $("button.search").click(function() {
                                $(".serch").slideToggle("slow");
                            });
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ✅ 登入區塊 -->
<div class="container">
    <section id="main">
        <div class="content">
            <div class="pag-nav">
                <ul class="p-list">
                    <li><a href="productList.action">Home</a></li> &nbsp;&nbsp;/&nbsp;
                    <li class="act">&nbsp;Login</li>
                </ul>
            </div>

            <div class="contact-box login-box">
                <h3 class="c-head text-center">會員登入</h3>

                <s:form action="login" method="post" cssClass="needs-validation" onsubmit="return validateLogin()">
                    <div class="form-group mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" name="email" id="email" class="form-control" value="${email}" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="password" class="form-label">密碼</label>
                        <input type="password" name="password" id="password" class="form-control" value="${password}" />
                    </div>

                    <!-- ✅ 置中按鈕 -->
                    <div class="form-group d-flex justify-content-center">
                        <s:submit value="登入" cssClass="btn btn-dark px-4" />
                    </div>
                </s:form>

                <!-- ✅ Google 登入與註冊連結區塊 -->
                <div class="text-center mt-4">
                    <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=29437975110-6rosalgpp5a2ncocs3cfgoqshiq182dt.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googleLoginCallback.action&response_type=code&scope=openid%20email%20profile">
                        <button class="btn btn-danger">使用 Google 登入</button>
                    </a>
                </div>
                <div class="text-center mt-2">
                    <a href="memberRegister.action">還沒註冊？馬上註冊</a>
                </div>
            </div>
        </div>
    </section>
</div>

</body>
</html>
