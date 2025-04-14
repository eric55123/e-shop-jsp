<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>註冊會員</title>
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
                alert("請輸入帳號！");
                return false;
            }

            if (password.length < 6) {
                alert("密碼至少要 6 碼！");
                return false;
            }

            if (phone !== "") {
                const phoneRegex = /^09\d{8}$/;
                if (!phoneRegex.test(phone)) {
                    alert("手機號碼格式錯誤，應為 09 開頭，共 10 碼！");
                    return false;
                }
            }

            return true;
        }
    </script>
</head>

<body>
<!-- 🔙 返回商品列表 -->
<div style="text-align:left;">
    <a href="productList.action">← 返回商品列表</a>
</div>

<h2>註冊會員</h2>




<br/><br/>

<!-- ✅ 本地註冊表單 -->
<s:form action="register" method="post" onsubmit="return validateForm()">
    <s:textfield name="member.email" label="電子郵件" required="true"/>
    <s:textfield name="member.username" label="帳號" required="true"/>
    <s:password name="member.password" label="密碼" required="true"/>
    <s:textfield name="member.phone" label="手機號碼"/>

    <div>
        <label>生日</label><br/>
        <input type="date" name="member.birthday"/>
    </div>

    <br/>
    <s:submit value="註冊"/>
</s:form>

<!-- ✅ Google 註冊按鈕區塊 -->
<a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=29437975110-6rosalgpp5a2ncocs3cfgoqshiq182dt.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googleLoginCallback.action&response_type=code&scope=openid%20email%20profile">
    <button style="background-color:#4285F4; color:white; padding:8px 16px; border:none; border-radius:4px; font-size:16px;">
        使用 Google 註冊
    </button>
</a>

<p><a href="login.action">已有帳號？登入</a></p>
</body>
</html>
