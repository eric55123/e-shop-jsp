<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>常見問題 FAQ</title>
    <jsp:include page="/includes/header.jsp" />

    <style>
        .faq-area {
            padding: 60px 0;
            font-family: "Segoe UI", sans-serif;
        }

        .faq-area .faq-title {
            text-align: center;
            margin-bottom: 40px;
            font-size: 28px;
        }

        .faq-area .accordion-item {
            border: 1px solid #ddd;
            border-radius: 0.5rem;
            margin: 20px auto;
            max-width: 800px;
            background-color: #fefefe;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }

        .faq-area .accordion-header {
            margin: 0;
        }

        .faq-area .accordion-button {
            background-color: #fff;
            color: #333;
            border: none;
            padding: 1.2rem 1.5rem;
            width: 100%;
            text-align: left;
            font-weight: 600;
            font-size: 18px;
            cursor: pointer;
            border-radius: 0.5rem;
            transition: all 0.3s ease;
            position: relative;
        }

        .faq-area .accordion-button::after {
            content: "▸";
            position: absolute;
            right: 20px;
            font-size: 18px;
            transition: transform 0.3s ease;
        }

        .faq-area .accordion-button.active::after {
            transform: rotate(90deg);
        }

        .faq-area .accordion-button:hover {
            background-color: #f4f7ff;
        }

        .faq-area .accordion-button.active {
            color: #0d6efd;
            background-color: #e7f1ff;
        }

        .faq-area .accordion-body {
            overflow: hidden;
            height: 0;
            opacity: 0;
            padding: 0 1.5rem;
            border-top: 1px solid #ddd;
            font-size: 16px;
            color: #555;
            line-height: 1.6;
        }

        .faq-area .accordion-body.show {
            padding: 1rem 1.5rem;
            opacity: 1;
        }

        .faq-area h4 {
            margin: 40px auto 10px;
            max-width: 800px;
            color: #0a58ca;
            font-size: 20px;
            font-weight: bold;
            display: flex;
            align-items: center;
        }

        .faq-area h4::before {
            content: "🔷";
            margin-right: 8px;
        }

        .faq-area .back-btn {
            text-align: center;
            margin-top: 40px;
        }

        .faq-area .back-btn a {
            text-decoration: none;
            padding: 0.5rem 1rem;
            border: 1px solid #ccc;
            border-radius: 0.25rem;
            color: #333;
            transition: all 0.2s;
        }

        .faq-area .back-btn a:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>

<body>
<div class="faq-area container">
    <div class="faq-title">
        <h2>常見問題</h2>
    </div>

    <s:iterator value="faqMap" var="entry" status="categoryStatus">
        <div class="mb-4">
            <h4><s:property value="#entry.key" /></h4>

            <div class="accordion" id="accordion-<s:property value="#categoryStatus.index" />">
                <s:iterator value="#entry.value" var="faq" status="faqStatus">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button" type="button"
                                    data-target="#collapse-<s:property value="#categoryStatus.index" />-<s:property value="#faqStatus.index" />">
                                Q：<s:property value="#faq.question" />
                            </button>
                        </h2>
                        <div id="collapse-<s:property value="#categoryStatus.index" />-<s:property value="#faqStatus.index" />"
                             class="accordion-body">
                            A：<c:out value="${faq.answer}" escapeXml="false"/>
                        </div>
                    </div>
                </s:iterator>
            </div>
        </div>
    </s:iterator>

    <div class="back-btn">
        <a href="productList.action">← 回商品列表</a>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll(".faq-area .accordion-button");

        buttons.forEach(btn => {
            btn.addEventListener("click", function () {
                const targetId = btn.getAttribute("data-target");
                const target = document.querySelector(targetId);
                const isOpen = target.classList.contains("show");

                if (!isOpen) {
                    expand(target);
                    btn.classList.add("active");
                } else {
                    collapse(target);
                    btn.classList.remove("active");
                }
            });
        });

        function expand(element) {
            element.classList.add("show");
            element.style.height = "auto";
            const endHeight = element.scrollHeight;
            element.style.height = "0px";

            element.animate([
                { height: "0px", paddingTop: "0px", paddingBottom: "0px", opacity: 0 },
                { height: endHeight + "px", paddingTop: "1rem", paddingBottom: "1rem", opacity: 1 }
            ], {
                duration: 300,
                easing: "ease"
            });

            element.style.height = endHeight + "px";
            element.style.opacity = "1";
            element.style.paddingTop = "1rem";
            element.style.paddingBottom = "1rem";
        }

        function collapse(element) {
            const startHeight = element.scrollHeight;

            // 🧼 先移除 .show → 避免高度觸發 transition
            element.classList.remove("show");

            // 🧱 動畫過程中不手動改 height，直接讓動畫接管
            const animation = element.animate([
                { height: startHeight + "px", paddingTop: "1rem", paddingBottom: "1rem", opacity: 1 },
                { height: "0px", paddingTop: "0px", paddingBottom: "0px", opacity: 0 }
            ], {
                duration: 300,
                easing: "ease"
            });

            animation.onfinish = () => {
                // ✅ 清除 style，避免殘留
                element.style.height = "";
                element.style.opacity = "";
                element.style.paddingTop = "";
                element.style.paddingBottom = "";
            };
        }
    });
</script>


<jsp:include page="/includes/footer.jsp" />
</body>
</html>
