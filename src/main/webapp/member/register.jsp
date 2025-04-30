<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/includes/header.jsp" />

<!-- ✅ jQuery & jQuery UI -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

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

                <!-- ✅ 顯示後端錯誤 -->
                <s:if test="hasActionErrors()">
                    <div class="alert alert-danger text-center">
                        <s:actionerror />
                    </div>
                </s:if>

                <!-- ✅ 顯示後端成功訊息 -->
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

                <!-- ✅ 註冊表單 -->
                <form action="register" method="post" onsubmit="return validateForm()">
                    <div class="form-group mb-3">
                        <label for="email">電子郵件</label>
                        <input type="text" name="member.email" id="email" class="form-control" value="${member.email}" />
                        <div class="text-danger small" id="emailError"></div>
                    </div>

                    <div class="form-group mb-3">
                        <label for="username">暱稱</label>
                        <input type="text" name="member.username" id="username" class="form-control" value="${member.username}" />
                        <div class="text-danger small" id="usernameError"></div>
                    </div>

                    <div class="form-group mb-3">
                        <label for="password">密碼</label>
                        <input type="password" name="member.password" id="password" class="form-control" />
                        <div class="text-danger small" id="passwordError"></div>
                    </div>

                    <div class="form-group mb-3">
                        <label for="phone">手機號碼</label>
                        <input type="text" name="member.phone" id="phone" class="form-control" value="${member.phone}" />
                        <div class="text-danger small" id="phoneError"></div>
                    </div>

                    <div class="form-group mb-4">
                        <label for="birthday">生日</label>
                        <input type="text" name="member.birthday" id="birthday" class="form-control" placeholder="yyyy-mm-dd" value="${member.birthday}" />
                        <div class="text-danger small" id="birthdayError"></div>
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

<!-- ✅ 驗證 + 日期初始化 -->
<script>
    function validateForm() {
        $(".text-danger").text(""); // 清空錯誤訊息
        let isValid = true;

        const email = $("#email").val().trim();
        const username = $("#username").val().trim();
        const password = $("#password").val().trim();
        const phone = $("#phone").val().trim();

        const emailRegex = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
        const phoneRegex = /^09\d{8}$/;
        const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;

        if (email === "") {
            $("#emailError").text("請輸入 Email！");
            isValid = false;
        } else if (!emailRegex.test(email)) {
            $("#emailError").text("Email 格式錯誤！");
            isValid = false;
        }

        if (username === "") {
            $("#usernameError").text("請輸入暱稱！");
            isValid = false;
        }

        if (password.length < 6) {
            $("#passwordError").text("密碼至少 6 碼！");
            isValid = false;
        }

        if (phone !== "" && !phoneRegex.test(phone)) {
            $("#phoneError").text("手機號碼格式錯誤！");
            isValid = false;
        }

        return isValid;
    }

    // ✅ 日期選擇器初始化
    $(function () {
        $("#birthday").datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true,
            changeYear: true,
            yearRange: "1900:c",
            maxDate: 0
        });
    });
</script>

<jsp:include page="/includes/footer.jsp" />
