<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<jsp:include page="/includes/header.jsp" />

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
                        <label for="birthday">生日</label>
                        <input type="text" name="member.birthday" id="birthday" class="form-control" value="${member.birthday}" placeholder="yyyy-mm-dd" />
                    </div>

                    <div class="form-group d-flex justify-content-center">
                        <button type="submit" class="btn btn-dark px-4">更新</button>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>

<!-- ✅ 驗證與 jQuery UI 日期選擇器與 Sticky Nav -->
<script>
    function validateMemberForm() {
        const username = document.querySelector('[name="member.username"]').value.trim();
        const phone = document.querySelector('[name="member.phone"]').value.trim();
        const birthday = document.querySelector('[name="member.birthday"]').value.trim();

        if (username.length > 50) {
            alert("暱稱不能超過 50 字！");
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
        // ✅ Sticky nav
        const stickyTop = $('#sticky_navigation').length ? $('#sticky_navigation').offset().top : 0;
        const stickyNav = function () {
            const scrollTop = $(window).scrollTop();
            if (scrollTop > stickyTop) {
                $('#sticky_navigation').css({ position: 'fixed', top: 0, left: 0 });
            } else {
                $('#sticky_navigation').css({ position: 'relative' });
            }
        };
        stickyNav();
        $(window).scroll(function () {
            stickyNav();
        });

        // ✅ Datepicker 生日選擇器
        $("#birthday").datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true,
            changeYear: true,
            yearRange: "1900:c",
            minDate: new Date(1900, 0, 1),
            maxDate: 0
        });
    });
</script>

<jsp:include page="/includes/footer.jsp" />
