<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>會員註冊</title>

    <!-- ✅ Bootstrap & 自訂樣式 -->
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" />

    <!-- ✅ jQuery & jQuery UI -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

    <script>
        function validateForm() {
            const email = document.querySelector('[name="member.email"]').value.trim();
            const username = document.querySelector('[name="member.username"]').value.trim();
            const password = document.querySelector('[name="member.password"]').value.trim();
            const phone = document.querySelector('[name="member.phone"]').value.trim();

            if (email === "") {
                alert("請輸入 Email！");
                return false;
            }

            const emailRegex = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
            if (!emailRegex.test(email)) {
                alert("Email 格式錯誤！");
                return false;
            }

            if (username === "") {
                alert("請輸入暱稱！");
                return false;
            }

            if (password.length < 6) {
                alert("密碼至少 6 碼！");
                return false;
            }

            if (phone !== "") {
                const phoneRegex = /^09\d{8}$/;
                if (!phoneRegex.test(phone)) {
                    alert("手機號碼格式錯誤！");
                    return false;
                }
            }

            return true;
        }

        $(function () {
            // ✅ 導覽列效果
            var sticky_navigation_offset_top = $('#sticky_navigation').offset()?.top || 0;
            var sticky_navigation = function () {
                var scroll_top = $(window).scrollTop();
                if (scroll_top > sticky_navigation_offset_top) {
                    $('#sticky_navigation').css({ 'position': 'fixed', 'top': 0, 'left': 0 });
                } else {
                    $('#sticky_navigation').css({ 'position': 'relative' });
                }
            };
            sticky_navigation();
            $(window).scroll(function () { sticky_navigation(); });

            $("span.menu").click(function () {
                $(".navig").slideToggle("slow");
            });

            $("button.search").click(function () {
                $(".serch").slideToggle("slow");
            });

            // ✅ 日期選擇器初始化
            $("#birthday").datepicker({
                dateFormat: "yy-mm-dd",
                changeMonth: true,
                changeYear: true,
                yearRange: "1900:c",
                maxDate: 0
            });
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

<!-- ✅ 註冊區塊 -->
<div class="container">
    <section id="main">
        <div class="content">
            <div class="pag-nav">
                <ul class="p-list">
                    <li><a href="productList.action">Home</a></li> &nbsp;&nbsp;/&nbsp;
                    <li class="act">&nbsp;Register</li>
                </ul>
            </div>

            <div class="contact-box login-box">
                <h3 class="c-head text-center">會員註冊</h3>

                <!-- ✅ 成功訊息提示與跳轉 -->
                <s:if test="hasActionMessages()">
                    <div class="alert alert-success text-center mt-3">
                        <s:actionmessage />
                        <script>
                            setTimeout(function () {
                                window.location.href = 'login.action';
                            }, 1500);
                        </script>
                    </div>
                </s:if>

                <!-- ✅ 表單開始 -->
                <form action="register" method="post" onsubmit="return validateForm()">
                    <div class="form-group mb-3">
                        <label for="email">電子郵件</label>
                        <input type="text" name="member.email" id="email" class="form-control" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="username">暱稱</label>
                        <input type="text" name="member.username" id="username" class="form-control" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="password">密碼</label>
                        <input type="password" name="member.password" id="password" class="form-control" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="phone">手機號碼</label>
                        <input type="text" name="member.phone" id="phone" class="form-control" />
                    </div>

                    <div class="form-group mb-4">
                        <label for="birthday">生日</label>
                        <input type="text" name="member.birthday" id="birthday" class="form-control" placeholder="yyyy-mm-dd" />
                    </div>

                    <div class="form-group d-flex justify-content-center">
                        <input type="submit" value="註冊" class="btn btn-dark px-4" />
                    </div>
                </form>

                <!-- ✅ Google 註冊 -->
                <div class="text-center mt-4">
                    <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=29437975110-6rosalgpp5a2ncocs3cfgoqshiq182dt.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googleLoginCallback.action&response_type=code&scope=openid%20email%20profile">
                        <button class="btn btn-danger">使用 Google 註冊</button>
                    </a>
                </div>
                <div class="text-center mt-2">
                    <a href="login.action">已經有帳號？立即登入</a>
                </div>
            </div>
        </div>
    </section>
</div>

</body>
</html>
