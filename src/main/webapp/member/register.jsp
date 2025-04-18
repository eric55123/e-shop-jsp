<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/includes/header.jsp" />

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

<!-- ✅ 客製化 JavaScript -->
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
