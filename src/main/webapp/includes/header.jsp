<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    com.eshop.member.model.Member loginMember = (com.eshop.member.model.Member) session.getAttribute("loginMember");
    String displayName = (loginMember != null)
            ? (loginMember.getName() != null ? loginMember.getName() : loginMember.getUsername())
            : null;
%>
<!DOCTYPE html>
<html>
<head>
    <title><s:property value="pageTitle" default="eShop" /></title>

    <!-- ✅ Bootstrap & 自訂樣式 -->
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" />

    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>

    <script>
        $(function () {
            var offsetTop = $('#sticky_navigation').offset()?.top || 0;
            const stickyNav = () => {
                const scrollTop = $(window).scrollTop();
                $('#sticky_navigation').css({ position: scrollTop > offsetTop ? 'fixed' : 'relative' });
            };
            stickyNav();
            $(window).on('scroll', stickyNav);
            $("span.menu").click(() => $(".navig").slideToggle("slow"));
            $("button.search").click(() => $(".serch").slideToggle("slow"));
        });
    </script>
</head>

<body style="background-color: #fff;">

<!-- ✅ 導覽列區塊 -->
<div class="header">
    <div class="container">
        <div id="demo_top_wrapper">
            <div id="sticky_navigation_wrapper">
                <div id="sticky_navigation">
                    <div class="demo_container navigation-bar">
                        <!-- 左邊 logo + 選單 -->
                        <div class="navigation">
                            <div class="logo">
                                <a href="<%=request.getContextPath()%>/productList.action">eShop</a>
                            </div>
                            <span class="menu"></span>
                            <div class="navig">
                                <ul>
                                    <li><a href="<%=request.getContextPath()%>/women.action">Woman</a></li>
                                    <li><a href="<%=request.getContextPath()%>/men.action">Men</a></li>
                                    <li><a href="#">Kids</a></li>
                                    <li><a href="#">即將到來</a></li>
                                    <li><a href="<%=request.getContextPath()%>/faqList.action">常見問題</a></li>
                                </ul>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <!-- 右邊使用者狀態 -->
                        <div class="navigation-right">
                            <ul class="user">
                                <% if (loginMember != null) { %>
                                <li><span class="user-icon"></span>歡迎，<strong><%= displayName %></strong></li>
                                <li><a href="<%=request.getContextPath()%>/logout.action">登出</a></li>
                                <% } else { %>
                                <li><a href="<%=request.getContextPath()%>/login.action">登入</a></li>
                                <li><a href="<%=request.getContextPath()%>/memberRegister.action">註冊</a></li>
                                <% } %>
                                <li><a href="<%=request.getContextPath()%>/cart.action"><span class="cart"></span>購物車</a></li>
                                <li><button class="search"></button></li>
                            </ul>
                        </div>

                        <div class="clearfix"></div>

                        <!-- 搜尋欄 -->
                        <div class="serch">
                            <span>
                                <input type="text" placeholder="Search" />
                                <input type="submit" value="" />
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
