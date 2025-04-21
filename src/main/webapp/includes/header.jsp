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

    <!-- ✅ jQuery -->
    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>

    <!-- ✅ Sticky nav JS -->
    <script>
        $(function () {
            var offsetTop = $('#sticky_navigation').offset()?.top || 0;
            const stickyNav = () => {
                const scrollTop = $(window).scrollTop();
                $('#sticky_navigation').css({ position: scrollTop > offsetTop ? 'fixed' : 'relative' });
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
                                    <li class="list-inline-item px-2"><a href="<%=request.getContextPath()%>/women.action">Woman</a></li>
                                    <li class="list-inline-item px-2"><a href="<%=request.getContextPath()%>/men.action">Men</a></li>
                                    <li class="list-inline-item px-2"><a href="#">Kids</a></li>
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
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/logout.action">登出</a></li>
                                <% } else { %>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/login.action">登入</a></li>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/memberRegister.action">註冊</a></li>
                                <% } %>
                                <li class="list-inline-item"><a href="<%=request.getContextPath()%>/cart.action">購物車</a></li>
                            </ul>
                        </div>
                    </div>
                    <!-- optional 分隔線 -->
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    /*!* ✅ 去除整體預設邊距 *!*/
    /*html, body {*/
    /*    margin: 0;*/
    /*    padding: 0;*/
    /*}*/

    /* ✅ 去除 container 上層 padding（讓 header 貼齊最頂） */
    .header {
        margin-top: 0 !important;
        padding-top: 0 !important;
    }
</style>
