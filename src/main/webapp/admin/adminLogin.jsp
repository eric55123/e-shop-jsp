<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>管理員登入</title>
    <style>
        body {
            font-family: "微軟正黑體", sans-serif;
            background-color: #f7f7f7;
            padding: 50px;
        }
        .login-box {
            width: 400px;
            margin: auto;
            padding: 30px;
            border: 1px solid #ccc;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .error {
            color: red;
            margin-bottom: 10px;
            text-align: center;
        }
        .test-accounts {
            margin-top: 30px;
            font-size: 14px;
            color: #333;
            background-color: #f0f0f0;
            padding: 15px;
            border-radius: 8px;
            border: 1px dashed #aaa;
        }
    </style>
</head>
<body>

<div class="login-box">
    <h2>後台管理員登入</h2>

    <!-- ✅ 只有當帳號有輸入過才顯示錯誤訊息 -->
    <s:if test="username != null && username.trim() != ''">
        <s:actionerror cssClass="error" />
    </s:if>

    <!-- 登入表單 -->
    <s:form action="adminLogin" method="post">
        <s:textfield name="username" label="帳號" required="true" />
        <s:password name="password" label="密碼" required="true" />
        <s:submit value="登入" />
    </s:form>

    <!-- ✅ 測試帳號提示區塊 -->
    <div class="test-accounts">
        <strong>🔑 測試帳號資訊：</strong><br>
        超管：<code>admin1</code>　密碼：<code>adminpwd1</code><br>
        其他：<code>admin2</code>　密碼：<code>adminpwd2</code><br>
        其他：<code>admin3</code>　密碼：<code>adminpwd3</code>
    </div>
</div>

</body>
</html>
