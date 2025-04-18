<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>會員資訊</title>

    <!-- ✅ Bootstrap & 自訂樣式 -->
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" type="text/css" />
    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>

    <script>
        function validateMemberForm() {
            const username = document.querySelector('[name="member.username"]').value.trim();
            const phone = document.querySelector('[name="member.phone"]').value.trim();
            const birthday = document.querySelector('[name="member.birthday"]').value.trim();

            if (username.length > 50) {
                alert("帳號不能超過 50 字！");
                return false;
            }

            if (phone !== "") {
                const phoneRegex = /^09\d{8}$/;
                if (!phoneRegex.test(phone)) {
                    alert("手機號碼格式錯誤，應為 09 開頭，共 10 碼！");
                    return false;
                }
            }

            if (birthday !== "") {
                const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
                if (!dateRegex.test(birthday)) {
                    alert("生日格式應為 yyyy-MM-dd！");
                    return false;
                }
            }

            return true;
        }

        $(function () {
            const stickyTop = $('#sticky_navigation').offset()?.top || 0;
            const stickyNav = function () {
                const scrollTop = $(window).scrollTop();
                if (scrollTop > stickyTop) {
                    $('#sticky_navigation').css({ position: 'fixed', top: 0, left: 0 });
                } else {
                    $('#sticky_navigation').css({ position: 'relative' });
                }
            };
            stickyNav();
            $(window).scroll(function () { stickyNav(); });
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
                                $("span.menu").click(function () {
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
                                <li><span></span><a href="logout.action">登出</a></li>
                                <li><span class="cart"></span><a href="cart.action">購物車</a></li>
                                <li><button class="search"></button></li>
                            </ul>
                        </div>
                        <div class="clearfix"></div>
                        <div class="serch">
                            <span>
                                <input type="text" placeholder="Search" />
                                <input type="submit" value="" />
                            </span>
                        </div>
                        <script>
                            $("button.search").click(function () {
                                $(".serch").slideToggle("slow");
                            });
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ✅ 主內容 -->
<div class="container">
    <section id="main">
        <div class="content">
            <div class="pag-nav">
                <ul class="p-list">
                    <li><a href="productList.action">Home</a></li> &nbsp;/&nbsp;
                    <li class="act">會員資訊</li>
                </ul>
            </div>

            <div class="contact-box login-box">
                <h3 class="c-head text-center">會員資訊（可修改）</h3>

                <form action="updateMember.action" method="post" onsubmit="return validateMemberForm()">
                    <div class="form-group mb-3">
                        <label for="email">Email</label>
                        <input type="text" name="member.email" id="email" class="form-control" value="${member.email}" readonly />
                    </div>

                    <div class="form-group mb-3">
                        <label for="username">暱稱</label>
                        <input type="text" name="member.username" id="username" class="form-control" value="${member.username}" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="phone">手機</label>
                        <input type="text" name="member.phone" id="phone" class="form-control" value="${member.phone}" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="birthday">生日 (yyyy-MM-dd)</label>
                        <input type="text" name="member.birthday" id="birthday" class="form-control" value="${member.birthday}" />
                    </div>

                    <div class="form-group d-flex justify-content-center">
                        <button type="submit" class="btn btn-dark px-4">更新</button>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>

</body>
</html>
