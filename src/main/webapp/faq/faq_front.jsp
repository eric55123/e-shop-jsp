<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>常見問題 FAQ</title>
    <style>
        body { font-family: sans-serif; }
        .category { font-size: 20px; margin-top: 30px; color: darkblue; }
        .faq-item { margin-bottom: 15px; }
        .question { font-weight: bold; font-size: 16px; color: #333; }
        .answer { margin-left: 20px; color: #555; }
    </style>
</head>
<body>
<h2>📌 常見問題</h2>

<s:iterator value="faqMap" var="entry">
    <div class="category">🔷 <s:property value="#entry.key" /></div>

    <s:iterator value="#entry.value" var="faq">
        <div class="faq-item">
            <div class="question">Q：<s:property value="#faq.question" /></div>
            <div class="answer">A：<c:out value="${faq.answer}" escapeXml="false"/></div>
        </div>
    </s:iterator>
</s:iterator>

<p><a href="productList.action">← 回商品列表</a></p>

</body>
</html>
