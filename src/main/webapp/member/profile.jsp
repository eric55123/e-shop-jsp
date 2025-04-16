<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>會員資訊</title>
    <script>
        function validateMemberForm() {
            const username = document.querySelector('[name="member.username"]').value.trim();
            const phone = document.querySelector('[name="member.phone"]').value.trim();
            const birthday = document.querySelector('[name="member.birthday"]').value.trim();

            // 帳號可為空但不能超過 50 字
            if (username.length > 50) {
                alert("帳號不能超過 50 字！");
                return false;
            }

            // 手機格式（可選填）
            if (phone !== "") {
                const phoneRegex = /^09\d{8}$/;
                if (!phoneRegex.test(phone)) {
                    alert("手機號碼格式錯誤，應為 09 開頭，共 10 碼！");
                    return false;
                }
            }

            // 生日格式（yyyy-MM-dd，可選填）
            if (birthday !== "") {
                const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
                if (!dateRegex.test(birthday)) {
                    alert("生日格式應為 yyyy-MM-dd！");
                    return false;
                }
            }

            return true;
        }
    </script>
</head>
<body>

<!-- 左上角返回 -->
<div style="text-align:left;">
    <a href="productList.action">← 返回商品列表</a>
</div>

<h2>會員資訊（可修改）</h2>

<!-- ✅ 加入驗證事件 -->
<s:form action="updateMember" method="post" onsubmit="return validateMemberForm()">
    <s:textfield name="member.email" label="Email" disabled="true"/>
    <s:textfield name="member.username" label="暱稱"/>
    <s:textfield name="member.phone" label="手機"/>
    <s:textfield name="member.birthday" label="生日 (yyyy-MM-dd)" />
    <s:submit value="更新" />
</s:form>

<p><a href="logout.action">登出</a></p>

</body>
</html>
