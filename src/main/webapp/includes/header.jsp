<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    com.eshop.member.model.Member loginMember = (com.eshop.member.model.Member) session.getAttribute("loginMember");
    String displayName = (loginMember != null)
            ? (loginMember.getName() != null ? loginMember.getName() : loginMember.getUsername())
            : null;
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><s:property value="pageTitle" default="eShop" /></title>

    <!-- ✅ Bootstrap & 自訂樣式 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/bootstrap.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/style.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/demo.css" />

    <!-- ✅ zmdi 圖示 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/material-design-iconic-font@2.2.0/dist/css/material-design-iconic-font.min.css">

    <!-- ✅ jQuery 3.6.0 -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- ✅ jQuery UI（如果要用日期選擇器） -->
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

    <!-- ✅ Sticky nav JS -->
    <script>
        $(function () {
            var offsetTop = $('#sticky_navigation').length ? $('#sticky_navigation').offset().top : 0;
            const stickyNav = function () {
                const scrollTop = $(window).scrollTop();
                if (scrollTop > offsetTop) {
                    $('#sticky_navigation').css({ position: 'fixed', top: 0, left: 0 });
                } else {
                    $('#sticky_navigation').css({ position: 'relative' });
                }
            };
            stickyNav();
            $(window).on('scroll', stickyNav);
        });
    </script>
</head>

<body style="background-color: #fff;">
<!-- ✅ 導覽列區塊 -->
<div class="header border-bottom">
    <div class="container">
        <div id="demo_top_wrapper">
            <div id="sticky_navigation_wrapper">
                <div id="sticky_navigation">
                    <div class="row align-items-center py-3">
                        <!-- LOGO + 選單 -->
                        <div class="col-md-8 d-flex align-items-center">
                            <div class="logo mr-4">
                                <a href="<%=request.getContextPath()%>/productList.action" class="h5 font-weight-bold text-dark">eShop</a>
                            </div>
                            <nav class="flex-grow-1">
                                <ul class="list-inline mb-0">
                                    <li class="list-inline-item px-2"><a href="#">即將到來</a></li>
                                    <li class="list-inline-item px-2"><a href="#">即將到來</a></li>
                                    <li class="list-inline-item px-2"><a href="#">即將到來</a></li>
                                    <li class="list-inline-item px-2"><a href="#">即將到來</a></li>
                                    <li class="list-inline-item px-2"><a href="<%=request.getContextPath()%>/faqList.action">常見問題</a></li>
                                </ul>
                            </nav>
                        </div>

                        <!-- 使用者狀態欄 -->
                        <div class="col-md-4 text-md-right mt-3 mt-md-0">
                            <ul class="list-inline mb-0">
                                <% if (loginMember != null) { %>
                                <li class="list-inline-item">歡迎，<strong><%= displayName %></strong></li>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/profile.action">個人資料</a></li>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/logout.action">登出</a></li>
                                <% } else { %>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/login.action">登入</a></li>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/memberRegister.action">註冊</a></li>
                                <% } %>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/cart.action">購物車</a></li>
                            </ul>
                        </div>
                    </div> <!-- ✅ 關閉 row -->
                </div> <!-- ✅ 關閉 sticky_navigation -->
            </div> <!-- ✅ 關閉 sticky_navigation_wrapper -->
        </div> <!-- ✅ 關閉 demo_top_wrapper -->
    </div> <!-- ✅ 關閉 container -->
</div> <!-- ✅ 關閉 header -->

<!-- ✅ 小修CSS：讓 Header 貼頂 -->
<style>
    .header {
        margin-top: 0 !important;
        padding-top: 0 !important;
    }
</style>
