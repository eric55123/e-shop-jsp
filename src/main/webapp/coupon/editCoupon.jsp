<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>修改優惠券</title>

    <!-- jQuery & UI -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

    <style>
        body {
            font-family: Arial;
            padding: 20px;
        }

        form {
            width: 500px;
        }

        .success {
            color: green;
            margin-bottom: 10px;
        }

        .error {
            color: red;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<h2>✏️ 修改優惠券</h2>

<!-- 顯示訊息 -->
<s:if test="hasActionErrors()">
    <div class="error">
        <s:actionerror/>
    </div>
</s:if>

<s:if test="message != null">
    <div class="success"><s:property value="message"/></div>
</s:if>

<s:form action="updateCoupon" method="post">
    <s:hidden name="coupon.couponId"/>

    <s:textfield name="coupon.couponCode" label="優惠券代碼"/>
    <s:textfield name="coupon.name" label="名稱"/>

    <s:select name="coupon.discountType" label="折扣類型"
              list="#{'fixed':'固定金額', 'percent':'百分比'}"
              headerKey="" headerValue="請選擇"/>

    <s:textfield name="coupon.discountValue" label="折扣數值" id="discountValue"/>
    <s:textfield name="coupon.minSpend" label="最低消費門檻" id="minSpend"/>
    <s:textfield name="coupon.validFromStr" label="開始日期" id="validFrom"/>
    <s:textfield name="coupon.validToStr" label="結束日期" id="validTo"/>

    <s:checkbox name="coupon.isEnabled" label="是否啟用" fieldValue="1"/>
    <s:textarea name="coupon.description" label="說明文字" cols="30" rows="3"/>

    <s:submit value="更新優惠券"/>
</s:form>

<!-- 日期初始化 -->
<script>
    $(function () {
        $("#validFrom, #validTo").datepicker({
            dateFormat: "yy-mm-dd"
        });
    });
</script>

<a href="<%= request.getContextPath() %>/back" class="btn btn-secondary">🔙 回後台首頁</a>

</body>
</html>
