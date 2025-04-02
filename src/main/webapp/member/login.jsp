<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>會員登入</title>
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
    </script>
</head>

<body>

<!-- ✅ 左上角加入商品列表連結 -->
<div style="text-align:left;">
    <a href="productList.action">← 返回商品列表</a>
</div>

<h2>會員登入</h2>

<!-- ✅ 加上 onsubmit -->
<s:form action="login" method="post" onsubmit="return validateLogin()">
    <s:textfield name="email" label="Email"/>
    <s:password name="password" label="密碼"/>
    <s:submit value="登入"/>
</s:form>

<p><a href="memberRegister.action">還沒註冊？馬上註冊</a></p>

</body>
</html>
